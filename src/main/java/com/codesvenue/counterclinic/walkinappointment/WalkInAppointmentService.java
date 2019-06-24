package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface WalkInAppointmentService {

    AppointmentStatus getAppointmentStatus(Integer appointmentId, Integer doctorId, LocalDateTime inquiryTime);

    AppointmentStatus callNextPatient(User user);

    WalkInAppointments getAllAppointments();

    boolean deleteAppointment(int appointmentId);

    QRCode getQrCodeForAppointment(int appointmentId);

    List<WalkInAppointmentWithAttachment> getAllWalkInAppointmentWithAttachment();

    WalkInAppointmentWrapper getWrappedAppointment(int appointmentId);
}
