package com.codesvenue.counterclinic.walkinappointment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class AppoinmentRepositoryMySqlTest {

    private AppointmentRepository appointmentRepository;

    @Before
    public void setup() {
        appointmentRepository = new AppointmentRepositoryMySql(TestData.getNamedParameterJdbcTemplate());
    }

    @Test
    public void itShouldFetchDoctorAppointmentsForToday() {
        Integer doctorId = 1;
        List<WalkInAppointment> walkInAppointmentList = appointmentRepository.fetchDoctorAppointmentsForToday(doctorId);
        Assert.assertNotNull(walkInAppointmentList);
        Assert.assertFalse(walkInAppointmentList.isEmpty());
    }
}
