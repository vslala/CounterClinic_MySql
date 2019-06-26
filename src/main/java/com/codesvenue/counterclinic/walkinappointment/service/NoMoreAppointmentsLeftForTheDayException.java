package com.codesvenue.counterclinic.walkinappointment.service;

public class NoMoreAppointmentsLeftForTheDayException extends RuntimeException {
    public NoMoreAppointmentsLeftForTheDayException(String msg) {
        super(msg);
    }
}
