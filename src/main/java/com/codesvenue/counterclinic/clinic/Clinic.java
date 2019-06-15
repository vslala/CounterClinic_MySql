package com.codesvenue.counterclinic.clinic;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class Clinic {

    private int clinicId;

    private String clinicName;
    private List<ClinicRoom> rooms;

    public Clinic(String clinicName) {
        this.clinicName = clinicName;
    }

    private Clinic(Clinic clinic) {
        this.clinicId = clinic.getClinicId();
        this.clinicName = clinic.getClinicName();
        this.rooms = clinic.getRooms();
    }

    public static Clinic newInstance(String clinicName) {
        return new Clinic(clinicName);
    }

    public static Clinic copyInstance(Clinic clinic) {
        return new Clinic(clinic);
    }

    public Clinic addNewRoom(ClinicRoom clinicRoom) {
        if (Objects.isNull(rooms))
            rooms = new LinkedList<>();
        rooms.add(clinicRoom);
        return this;
    }

    public List<ClinicRoom> getClinicRooms() {
        return Collections.unmodifiableList(rooms);
    }
}
