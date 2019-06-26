package com.codesvenue.counterclinic.walkinappointment.model;

public class AppointedUserIsNotADoctor extends RuntimeException {
    public AppointedUserIsNotADoctor(String msg) {
        super(msg);
    }
}
