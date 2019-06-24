package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.qrcode.QRCode;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
