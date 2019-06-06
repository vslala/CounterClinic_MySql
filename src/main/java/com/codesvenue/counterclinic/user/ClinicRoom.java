package com.codesvenue.counterclinic.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ClinicRoom {

    private String name;

    public ClinicRoom(String name) {
        this.name = name;
    }

    public static ClinicRoom newInstance(String name) {
        return new ClinicRoom(name);
    }
}
