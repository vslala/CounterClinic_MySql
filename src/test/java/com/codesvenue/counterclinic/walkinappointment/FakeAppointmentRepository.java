package com.codesvenue.counterclinic.walkinappointment;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Repository
@Log4j
public class FakeAppointmentRepository implements AppointmentRepository {
    @Override
    public List<WalkInAppointment> fetchDoctorAppointmentsForToday(Integer doctorId) {
        return TestData.buildWalkInAppointments(10, 15);
    }

    @Override
    public List<AppointmentStatus> fetchAppointmentStatusList(Integer doctorId) {
        return TestData.appointmentStatusList;
    }

    @Override
    public AppointmentStatus saveAppointmentStatus(AppointmentStatus appointmentStatus) {
        appointmentStatus.setAppointmentStatusId(TestData.appointmentStatusList.size()+1);
        TestData.store(appointmentStatus);
        return appointmentStatus;
    }
}
