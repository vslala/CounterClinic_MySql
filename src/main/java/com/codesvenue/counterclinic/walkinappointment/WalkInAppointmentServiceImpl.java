package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.User;
import lombok.extern.log4j.Log4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

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

    @Override
    public AppointmentStatus callNextPatient(User user) {
        List<WalkInAppointment> walkInAppointmentList = appointmentRepository.fetchDoctorAppointmentsForToday(user.getUserId());
        List<AppointmentStatus> appointmentStatusList = appointmentRepository.fetchAppointmentStatusList(user.getUserId());
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        AppointmentStatus newAppointmentStatus = null;
        if (appointmentStatusList.isEmpty()) {
            WalkInAppointment firstAppointment = walkInAppointmentList.get(0);
            newAppointmentStatus = AppointmentStatus.newInstanceForFirstTime(firstAppointment.getWalkInAppointmentId(),
                    user.getUserId(), WalkInAppointment.findPatientsBeforeGivenAppointmentId(firstAppointment.getWalkInAppointmentId(),
                            walkInAppointmentList), now);
        } else {
            AppointmentStatus currentAppointmentStatus = appointmentStatusList.get(appointmentStatusList.size()-1);
            newAppointmentStatus = currentAppointmentStatus.generateAppointmentStatus(walkInAppointmentList, now);
        }

        AppointmentStatus appointmentStatus = appointmentRepository.saveAppointmentStatus(newAppointmentStatus);

        user.askReceptionistToSendNextPatient(newAppointmentStatus.getCurrentAppointmentId(), simpMessagingTemplate);
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
        return walkInAppointmentWrapper;
    }

}
