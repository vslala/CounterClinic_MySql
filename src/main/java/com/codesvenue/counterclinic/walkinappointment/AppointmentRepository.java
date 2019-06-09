package com.codesvenue.counterclinic.walkinappointment;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository {
    List<WalkInAppointment> fetchDoctorAppointmentsForToday(Integer doctorId);

    List<AppointmentStatus> fetchAppointmentStatusList(Integer doctorId);

    AppointmentStatus saveAppointmentStatus(AppointmentStatus appointmentStatus);
}
