package com.codesvenue.counterclinic.user;

import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class User {

    private int userId;

    @Setter
    private List<UserRole> roles;

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
}
