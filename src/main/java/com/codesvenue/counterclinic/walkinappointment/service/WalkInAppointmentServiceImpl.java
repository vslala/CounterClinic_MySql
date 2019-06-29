package com.codesvenue.counterclinic.walkinappointment.service;

import com.codesvenue.counterclinic.configuration.DateTimeConstants;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.walkinappointment.dao.AppointmentRepository;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentWithAttachment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentWrapper;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointments;
import lombok.extern.log4j.Log4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Log4j
public class WalkInAppointmentServiceImpl implements WalkInAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public WalkInAppointmentServiceImpl(AppointmentRepository appointmentRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public AppointmentStatus getAppointmentStatus(Integer appointmentId, Integer doctorId, LocalDateTime inquiryTime) {
        List<WalkInAppointment> walkInAppointmentList = appointmentRepository.fetchDoctorAppointmentsForToday(doctorId);
        List<AppointmentStatus> appointmentStatusList = appointmentRepository.fetchAppointmentStatusList(doctorId);
        int patientsBeforeThisAppointmentId = WalkInAppointment.findPatientsBeforeGivenAppointmentId(appointmentId, walkInAppointmentList);

        if (appointmentStatusList.isEmpty()) {
            // doctor has not started taking patients
            return AppointmentStatus.newInstanceForFirstTime(appointmentId, doctorId, patientsBeforeThisAppointmentId, LocalDateTime.now(ZoneOffset.UTC));
        }

        AppointmentStatus lastAppointmentStatus = appointmentStatusList.get(appointmentStatusList.size()-1);
        int approxAvgWaitTime = lastAppointmentStatus.calculateAvgWaitingTime(inquiryTime, patientsBeforeThisAppointmentId);

        return AppointmentStatus.newInstanceWithApproxAvgWaitTime(appointmentId, approxAvgWaitTime, lastAppointmentStatus);
    }

    @Transactional
    @Override
    public AppointmentStatus callNextPatient(User user) {

        WalkInAppointment nextWalkInAppointment = getNextAppointment(user);
        Optional<AppointmentStatus> latestAppointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorId(user.getUserId());
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        AppointmentStatus newAppointmentStatus = null;
        if (latestAppointmentStatus.isPresent()) {
            newAppointmentStatus = latestAppointmentStatus.get().generateAppointmentStatus(nextWalkInAppointment, now);
        } else {
            newAppointmentStatus = AppointmentStatus.newInstanceForFirstTime(nextWalkInAppointment.getWalkInAppointmentId(),
                    user.getUserId(), 0, now);
        }

        AppointmentStatus appointmentStatus = appointmentRepository.saveAppointmentStatus(newAppointmentStatus);
        user.askReceptionistToSendNextPatient(appointmentStatus, simpMessagingTemplate);
        return appointmentStatus;
    }

    @Override
    public WalkInAppointments getAllAppointments() {
        WalkInAppointments walkInAppointments = appointmentRepository.fetchAllWalkInAppointments();
        return walkInAppointments;
    }

    @Transactional
    @Override
    public boolean deleteAppointment(int appointmentId) {
        int rowsAffected = appointmentRepository.deleteWalkInAppointment(appointmentId);
        QRCode qrCode = appointmentRepository.deleteQrCodeAttachment(appointmentId);
        Paths.get(qrCode.getQrCodeFilePath()).toFile().delete();
        return rowsAffected > 0;
    }

    @Override
    public QRCode getQrCodeForAppointment(int appointmentId) {
        return appointmentRepository.fetchQrCodeAttachment(appointmentId);
    }

    @Override
    public List<WalkInAppointmentWithAttachment> getAllWalkInAppointmentWithAttachment() {
        return appointmentRepository.fetchAllWalkInAppointmentsWithAttachments();
    }

    @Transactional
    @Override
    public WalkInAppointmentWrapper getWrappedAppointment(int appointmentId) {
        WalkInAppointmentWrapper walkInAppointmentWrapper = appointmentRepository.findWalkInAppointmentById(appointmentId);
        walkInAppointmentWrapper.setAppointmentStatus(getAppointmentStatus(
                walkInAppointmentWrapper.getWalkInAppointment().getWalkInAppointmentId(),
                walkInAppointmentWrapper.getAppointedDoctor().getUserId(),
                LocalDateTime.parse(walkInAppointmentWrapper.getWalkInAppointment().getCreatedAt(), DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN))
        ));
        return walkInAppointmentWrapper;
    }

    @Transactional
    @Override
    public WalkInAppointment getNextAppointment(User user) {
        List<WalkInAppointment> walkInAppointmentList = appointmentRepository.fetchDoctorAppointmentsForToday(user.getUserId());
        if (walkInAppointmentList.isEmpty()) {
            throw new EmptyWalkInAppointmentException("There are no walkin appointments for the day.");
        }

        Optional<AppointmentStatus> latestAppointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorId(user.getUserId());
        if (latestAppointmentStatus.isPresent()) {
            int nextAppointmentIndex = latestAppointmentStatus.get().getPatientsInVisitedQueue() + 1;
            if (nextAppointmentIndex > walkInAppointmentList.size()) {
                throw new NoMoreAppointmentsLeftForTheDayException("Doctor has checked all his patients.");
            }
            if (latestAppointmentStatus.isPresent()) {
                return walkInAppointmentList.get(nextAppointmentIndex);
            }
        }


        return walkInAppointmentList.get(0);
    }

    @Override
    public AppointmentStatus getLatestAppointmentStatus(User doctor) {
        Optional<AppointmentStatus> appointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorId(doctor.getUserId());
        return appointmentStatus.isPresent() ? appointmentStatus.get() : new AppointmentStatus();
    }

    @Transactional
    @Override
    public AppointmentStatus doctorTakesBreak(User user, int breakDuration) {
        Optional<AppointmentStatus> latestAppointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorId(user.getUserId());
        AppointmentStatus newAppointmentStatus = null;

        if (latestAppointmentStatus.isPresent()) {
            newAppointmentStatus = user.takeBreak(latestAppointmentStatus.get(), breakDuration);
        } else {
            newAppointmentStatus = user.takeBreak(AppointmentStatus.newInstanceForFirstTime(
                            0,
                            user.getUserId(),
                            -1,
                            LocalDateTime.now(ZoneOffset.UTC)),
                    breakDuration);
        }

        newAppointmentStatus = appointmentRepository.saveAppointmentStatus(newAppointmentStatus);
        user.broadcastNewState("/topic/appointment-status", newAppointmentStatus, simpMessagingTemplate);
        return newAppointmentStatus;
    }

}
