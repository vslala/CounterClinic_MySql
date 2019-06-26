package com.codesvenue.counterclinic.walkinappointment.dao;

public class NoMoreAppointmentsException extends RuntimeException {
    public NoMoreAppointmentsException(String msg) {
        super(msg);
    }
}
