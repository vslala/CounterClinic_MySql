package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.clinic.ClinicRoomNotAssignedToDoctorException;
import com.codesvenue.counterclinic.user.User;
import com.codesvenue.counterclinic.user.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    private String createdAt;

    private WalkInAppointment(WalkInAppointment other) {
        this.walkInAppointmentId = other.walkInAppointmentId;
        this.patientFirstName = other.patientFirstName;
        this.patientLastName = other.patientLastName;
        this.appointedDoctorId = other.appointedDoctorId;
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
            .createdAt(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
                    .replace("T", " "));
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
}
