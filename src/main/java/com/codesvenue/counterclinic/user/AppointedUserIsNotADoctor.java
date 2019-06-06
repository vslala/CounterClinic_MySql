package com.codesvenue.counterclinic.user;

public class AppointedUserIsNotADoctor extends RuntimeException {
    public AppointedUserIsNotADoctor(String msg) {
        super(msg);
    }
}
