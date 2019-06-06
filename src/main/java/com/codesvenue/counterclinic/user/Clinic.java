package com.codesvenue.counterclinic.user;

import lombok.Data;
import lombok.Getter;
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

    public static Clinic newInstance(String clinicName) {
        return new Clinic(clinicName);
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
