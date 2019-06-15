package com.codesvenue.counterclinic.walkinappointment;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppointmentRepositoryMySql implements AppointmentRepository{
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AppointmentRepositoryMySql(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<WalkInAppointment> fetchDoctorAppointmentsForToday(Integer doctorId) {
        return null;
    }

    @Override
    public List<AppointmentStatus> fetchAppointmentStatusList(Integer doctorId) {
        return null;
    }

    @Override
    public AppointmentStatus saveAppointmentStatus(AppointmentStatus appointmentStatus) {
        return null;
    }
}
