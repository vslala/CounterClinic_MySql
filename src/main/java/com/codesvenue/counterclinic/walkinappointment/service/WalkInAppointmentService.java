package com.codesvenue.counterclinic.walkinappointment.service;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.dao.UserRepository;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.service.UserService;
import com.codesvenue.counterclinic.walkinappointment.model.*;
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

    WalkInAppointment getNextAppointment(User user);

    AppointmentStatus getLatestAppointmentStatus(User doctor);

    AppointmentStatus doctorTakesBreak(User user, int breakDuration);

    WalkInAppointment createNewWalkInAppointment(User loggedInUser, WalkInAppointmentInfoForm walkInAppointmentInfoForm);

    AppointmentStatus getLatestAppointmentStatus(User userId, String day);

    void setUserRepository(UserRepository userRepository);
}
