package com.codesvenue.counterclinic.walkinappointment.dao;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.walkinappointment.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository {
    List<WalkInAppointment> fetchDoctorAppointmentsForToday(Integer doctorId);

    List<AppointmentStatus> fetchAppointmentStatusList(Integer doctorId);

    AppointmentStatus saveAppointmentStatus(AppointmentStatus appointmentStatus);

    WalkInAppointments fetchAllWalkInAppointments();

    int deleteWalkInAppointment(int appointmentId);

    QRCode deleteQrCodeAttachment(int appointmentId);

    QRCode fetchQrCodeAttachment(int appointmentId);

    List<WalkInAppointmentWithAttachment> fetchAllWalkInAppointmentsWithAttachments();

    WalkInAppointmentWrapper findWalkInAppointmentById(int appointmentId);

    Optional<AppointmentStatus> findLatestAppointmentStatusByDoctorId(Integer userId);
}
