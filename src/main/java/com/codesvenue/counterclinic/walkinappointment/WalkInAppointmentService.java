package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface WalkInAppointmentService {

    AppointmentStatus getAppointmentStatus(Integer appointmentId, Integer doctorId, LocalDateTime inquiryTime);

    AppointmentStatus callNextPatient(User user);
}
