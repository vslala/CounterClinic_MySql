package com.codesvenue.counterclinic.clinic.model;

import com.codesvenue.counterclinic.user.model.User;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@Log4j
@Data
public class AssignClinicForm {

    private User selectedDoctor;
    private ClinicRoom selectedClinicRoom;

    public static AssignClinicForm newInstance() {
        return new AssignClinicForm();
    }

    public AssignClinicForm selectedDoctor(User selectedDoctor) {
        this.selectedDoctor = selectedDoctor;
        return this;
    }

    public AssignClinicForm selectedClinicRoom(ClinicRoom selectedClinicRoom) {
        this.selectedClinicRoom = selectedClinicRoom;
        return this;
    }
}
