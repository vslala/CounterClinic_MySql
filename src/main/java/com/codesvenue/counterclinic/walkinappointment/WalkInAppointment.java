package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.clinic.ClinicRoomNotAssignedToDoctorException;
import com.codesvenue.counterclinic.user.User;
import com.codesvenue.counterclinic.user.UserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
public class WalkInAppointment {

    private Integer walkInAppointmentId;
    private String patientFirstName;
    private String patientLastName;
    private User appointedDoctor;
    private String createdAt;

    public WalkInAppointment create(String patientFirstName, String patientLastName, User appointedDoctor) {
        if (! isDoctor(appointedDoctor))
            throw new AppointedUserIsNotADoctor("Appointed user is not a doctor.");
        if (isRoomAssignedToDoctor(appointedDoctor))
            throw new ClinicRoomNotAssignedToDoctorException("Doctor must be assigned a clinic before making appointment.");

        return WalkInAppointment.newInstance()
            .patientFirstName(patientFirstName)
            .patientLastName(patientLastName)
            .appointedDoctor(appointedDoctor)
            .createdAt(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)
                    .replace("T", " "));
    }

    private WalkInAppointment createdAt(String dateTime) {
        this.createdAt = dateTime;
        return this;
    }

    private WalkInAppointment appointedDoctor(User appointedDoctor) {
        this.appointedDoctor = appointedDoctor;
        return this;
    }

    private WalkInAppointment patientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
        return this;
    }

    private WalkInAppointment patientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
        return this;
    }

    private static WalkInAppointment newInstance() {
        return new WalkInAppointment();
    }

    private boolean isDoctor(User appointedDoctor) {
        return appointedDoctor.getRoles().contains(UserRole.DOCTOR);
    }

    private boolean isRoomAssignedToDoctor(User appointedDoctor) {
        return Objects.isNull(appointedDoctor.getAssignedClinicRoom());
    }
}
