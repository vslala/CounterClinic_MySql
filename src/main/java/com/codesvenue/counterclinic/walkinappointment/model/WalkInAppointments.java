package com.codesvenue.counterclinic.walkinappointment.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Data
@Log4j
@NoArgsConstructor
public class WalkInAppointments {
    List<WalkInAppointment> walkInAppointmentList;

    public static class WalkInAppointmentsRowMapper implements RowMapper<WalkInAppointment> {

        public static WalkInAppointmentsRowMapper newInstance() {
            return new WalkInAppointmentsRowMapper();
        }


        @Override
        public WalkInAppointment mapRow(ResultSet resultSet, int i) throws SQLException {
            WalkInAppointment walkInAppointment = new WalkInAppointment();
            walkInAppointment.setWalkInAppointmentId(resultSet.getInt("walkin_appointment_id"));
            walkInAppointment.setPatientFirstName(resultSet.getString("patient_first_name"));
            walkInAppointment.setPatientLastName(resultSet.getString("patient_last_name"));
            walkInAppointment.setAppointedDoctorId(resultSet.getInt("appointed_doctor_id"));
            walkInAppointment.setCreatedAt(resultSet.getString("created_at"));
            return walkInAppointment;
        }
    }
}
