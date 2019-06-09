package com.codesvenue.counterclinic.walkinappointment;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface WalkInAppointmentService {

    AppointmentStatus getAppointmentStatus(Integer appointmentId, Integer doctorId, LocalDateTime inquiryTime);
}
