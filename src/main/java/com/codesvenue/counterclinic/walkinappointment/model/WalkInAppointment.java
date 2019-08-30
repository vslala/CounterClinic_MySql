package com.codesvenue.counterclinic.walkinappointment.model;

import com.codesvenue.counterclinic.clinic.model.ClinicRoomNotAssignedToDoctorException;
import com.codesvenue.counterclinic.configuration.DateTimeConstants;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class WalkInAppointment {

    private Integer walkInAppointmentId;
    private String patientFirstName;
    private String patientLastName;
    private Integer appointedDoctorId;
    private Integer appointmentNumber;
    private String createdAt;

    private WalkInAppointment(WalkInAppointment other) {
        this.walkInAppointmentId = other.walkInAppointmentId;
        this.patientFirstName = other.patientFirstName;
        this.patientLastName = other.patientLastName;
        this.appointedDoctorId = other.appointedDoctorId;
        this.appointmentNumber = other.appointmentNumber;
        this.createdAt = other.createdAt;
    }

    public static WalkInAppointment copyInstance(WalkInAppointment walkInAppointment) {
        return new WalkInAppointment(walkInAppointment);
    }

    public WalkInAppointment create(String patientFirstName, String patientLastName, User appointedDoctor) {
        if (! isDoctor(appointedDoctor))
            throw new AppointedUserIsNotADoctor("Appointed user is not a doctor.");
        if (isRoomAssignedToDoctor(appointedDoctor))
            throw new ClinicRoomNotAssignedToDoctorException("Doctor must be assigned a clinic before making appointment.");

        return WalkInAppointment.newInstance()
            .patientFirstName(patientFirstName)
            .patientLastName(patientLastName)
            .appointedDoctor(appointedDoctor.getUserId())
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)));
    }

    public WalkInAppointment createdAt(String dateTime) {
        this.createdAt = dateTime;
        return this;
    }

    public WalkInAppointment appointedDoctor(Integer appointedDoctorId) {
        this.appointedDoctorId = appointedDoctorId;
        return this;
    }

    public WalkInAppointment patientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
        return this;
    }

    public WalkInAppointment patientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
        return this;
    }

    public WalkInAppointment appointmentNumber(Integer appointmentNumber) {
        this.appointmentNumber = appointmentNumber;
        return this;
    }

    public WalkInAppointment walkInAppointmentId(Integer id) {
        this.walkInAppointmentId = id;
        return this;
    }

    private static WalkInAppointment newInstance() {
        return new WalkInAppointment();
    }

    private boolean isDoctor(User appointedDoctor) {
        return appointedDoctor.getRoles().contains(UserRole.DOCTOR);
    }

    private boolean isRoomAssignedToDoctor(User appointedDoctor) {
        return Objects.isNull(appointedDoctor.getClinicRoomId());
    }

    public static int findPatientsBeforeGivenAppointmentId(Integer appointmentId, List<WalkInAppointment> walkInAppointmentList) {
        int patientsBeforeThisAppointmentId = 0;
        for (WalkInAppointment walkInAppointment: walkInAppointmentList) {
            if (walkInAppointment.getWalkInAppointmentId().equals(appointmentId)) {
                break;
            }
            patientsBeforeThisAppointmentId++;
        }
        return patientsBeforeThisAppointmentId;
    }

    public String getPatientFullName() {
        return this.patientFirstName + " " + this.patientLastName;
    }

    public static class WalkInAppointmentRowMapper implements RowMapper<WalkInAppointment> {
        public static WalkInAppointmentRowMapper newInstance() {
            return new WalkInAppointmentRowMapper();
        }

        @Override
        public WalkInAppointment mapRow(ResultSet resultSet, int i) throws SQLException {
            WalkInAppointment walkInAppointment = new WalkInAppointment();
            walkInAppointment.setWalkInAppointmentId(resultSet.getInt("walkin_appointment_id"));
            walkInAppointment.setPatientFirstName(resultSet.getString("patient_first_name"));
            walkInAppointment.setPatientLastName(resultSet.getString("patient_last_name"));
            walkInAppointment.setAppointedDoctorId(resultSet.getInt("appointed_doctor_id"));
            walkInAppointment.setAppointmentNumber(resultSet.getInt("appointment_number"));
            walkInAppointment.setCreatedAt(resultSet.getString("created_at"));
            return walkInAppointment;
        }
    }
}
