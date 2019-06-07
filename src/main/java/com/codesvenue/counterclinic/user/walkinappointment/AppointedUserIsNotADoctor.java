package com.codesvenue.counterclinic.user.walkinappointment;

public class AppointedUserIsNotADoctor extends RuntimeException {
    public AppointedUserIsNotADoctor(String msg) {
        super(msg);
    }
}
