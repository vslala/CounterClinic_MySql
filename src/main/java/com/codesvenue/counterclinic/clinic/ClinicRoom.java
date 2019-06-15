package com.codesvenue.counterclinic.clinic;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ClinicRoom {

    private String name;

    public ClinicRoom(String name) {
        this.name = name;
    }

    private ClinicRoom(ClinicRoom room) {
        this.name = room.getName();
    }

    public static ClinicRoom newInstance(String name) {
        return new ClinicRoom(name);
    }

    public static ClinicRoom copyInstance(ClinicRoom room) {
        return new ClinicRoom(room);
    }
}
