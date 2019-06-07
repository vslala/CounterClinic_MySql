package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicRoom;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User {

    private Integer userId;

    @Setter
    private List<UserRole> roles;

    @Getter
    private ClinicRoom assignedClinicRoom;

    public List<UserRole> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public Clinic createNewClinic(String clinicName) {
        if (isSuperAdmin())
            return Clinic.newInstance(clinicName);
        throw new ActionNotAllowedException("User do not have privileges to create new clinic.");
    }

    private boolean isSuperAdmin() {
        return !Objects.isNull(roles) && this.roles.contains(UserRole.SUPERADMIN);
    }

    public Clinic addClinicRoomToClinic(Clinic clinic, ClinicRoom clinicRoom) {
        if (isSuperAdmin())
            return clinic.addNewRoom(clinicRoom);
        throw new ActionNotAllowedException("User do not have privileges to add new room to clinic.");
    }

    public boolean assignClinicRoom(ClinicRoom clinicRoom) {
        if (isSuperAdminOrReceptionist()) {
            this.assignedClinicRoom = clinicRoom;
            return !Objects.isNull(this.assignedClinicRoom);
        }
        throw new ActionNotAllowedException("User do not have privileges to assign clinic rooms to doctor.");
    }

    private boolean isSuperAdminOrReceptionist() {
        return !Objects.isNull(roles) &&
                (this.roles.contains(UserRole.RECEPTIONIST) || this.roles.contains(UserRole.SUPERADMIN));
    }

    public WalkInAppointment createWalkInAppointment(String patientFirstName, String patientLastName, User appointedDoctor) {
        if (isReceptionist())
            return new WalkInAppointment().create(patientFirstName, patientLastName, appointedDoctor);
        throw new ActionNotAllowedException("Only receptionists are allowed to create Walk In Appointments for patients");
    }

    private boolean isReceptionist() {
        return !Objects.isNull(roles) && roles.contains(UserRole.RECEPTIONIST);
    }

    public void setClinicRoom(ClinicRoom clinicRoom) {
        this.assignedClinicRoom = clinicRoom;
    }
}
