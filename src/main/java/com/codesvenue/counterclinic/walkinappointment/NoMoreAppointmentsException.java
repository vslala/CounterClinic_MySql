package com.codesvenue.counterclinic.walkinappointment;

public class NoMoreAppointmentsException extends RuntimeException {
    public NoMoreAppointmentsException(String msg) {
        super(msg);
    }
}
