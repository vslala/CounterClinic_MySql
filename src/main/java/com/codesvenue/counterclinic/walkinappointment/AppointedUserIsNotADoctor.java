package com.codesvenue.counterclinic.walkinappointment;

public class AppointedUserIsNotADoctor extends RuntimeException {
    public AppointedUserIsNotADoctor(String msg) {
        super(msg);
    }
}
