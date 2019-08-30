package com.codesvenue.counterclinic.walkinappointment.service;

import com.codesvenue.counterclinic.configuration.DateTimeConstants;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.dao.UserRepository;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.service.UserService;
import com.codesvenue.counterclinic.walkinappointment.dao.AppointmentRepository;
import com.codesvenue.counterclinic.walkinappointment.model.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j
public class WalkInAppointmentServiceImpl implements WalkInAppointmentService {
    private static final int FIRST = 0;
    private final AppointmentRepository appointmentRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private UserRepository userRepository;

    public WalkInAppointmentServiceImpl(AppointmentRepository appointmentRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public AppointmentStatus getAppointmentStatus(Integer appointmentId, Integer doctorId, LocalDateTime inquiryTime) {
        List<WalkInAppointment> walkInAppointmentList = appointmentRepository.fetchDoctorAppointmentsForToday(doctorId);
        List<AppointmentStatus> appointmentStatusList = appointmentRepository.fetchAppointmentStatusListForToday(doctorId);
        int patientsBeforeThisAppointmentId = WalkInAppointment.findPatientsBeforeGivenAppointmentId(appointmentId, walkInAppointmentList);

        if (appointmentStatusList.isEmpty()) {
            // doctor has not started taking patients
            return AppointmentStatus.newInstanceForFirstTime(appointmentId, doctorId, patientsBeforeThisAppointmentId, LocalDateTime.now());
        }

        AppointmentStatus latestAppointmentStatus = appointmentStatusList.get(FIRST);
        int approxAvgWaitTime = latestAppointmentStatus.calculateAvgWaitingTime(inquiryTime, patientsBeforeThisAppointmentId);

        return AppointmentStatus.newInstanceWithApproxAvgWaitTime(appointmentId, approxAvgWaitTime, latestAppointmentStatus);
    }

    @Transactional
    @Override
    public AppointmentStatus callNextPatient(User user) {

        WalkInAppointment nextWalkInAppointment = getNextAppointment(user);
        Optional<AppointmentStatus> latestAppointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorIdForToday(user.getUserId());
        LocalDateTime now = LocalDateTime.now();
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

        Optional<AppointmentStatus> latestAppointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorIdForToday(user.getUserId());
        if (latestAppointmentStatus.isPresent()) {
            int nextAppointmentIndex = latestAppointmentStatus.get().getPatientsInVisitedQueue();
            if (nextAppointmentIndex >= walkInAppointmentList.size()) {
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
        Optional<AppointmentStatus> appointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorIdForToday(doctor.getUserId());
        return appointmentStatus.isPresent() ? appointmentStatus.get() : new AppointmentStatus();
    }

    @Override
    public AppointmentStatus getLatestAppointmentStatus(User doctor, String day) {
        if (!Objects.isNull(day) && day.toLowerCase().equals("today")) {
            Optional<AppointmentStatus> appointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorIdForToday(doctor.getUserId());
            return appointmentStatus.isPresent() ? appointmentStatus.get() : new AppointmentStatus();
        }
        return getLatestAppointmentStatus(doctor);
    }

    @Transactional
    @Override
    public AppointmentStatus doctorTakesBreak(User user, int breakDuration) {
        Optional<AppointmentStatus> latestAppointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorIdForToday(user.getUserId());
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

    @Transactional
    @Override
    public WalkInAppointment createNewWalkInAppointment(User loggedInUser, WalkInAppointmentInfoForm walkInAppointmentInfoForm) {
        WalkInAppointment walkInAppointment = loggedInUser.createWalkInAppointment(
                walkInAppointmentInfoForm.getPatientFirstName(),
                walkInAppointmentInfoForm.getPatientLastName(),
                userRepository.findUserById(walkInAppointmentInfoForm.getDoctorId()),
                appointmentRepository.fetchDoctorAppointmentsForToday(walkInAppointmentInfoForm.getDoctorId()));
        WalkInAppointment newWalkInAppointment = appointmentRepository.createNewWalkInAppointment(walkInAppointment);
        QRCode qrCode = QRCode.Generator.generateQRCode(newWalkInAppointment);
        appointmentRepository.createNewQRCode(qrCode);
        return newWalkInAppointment;
    }

    @Override
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
