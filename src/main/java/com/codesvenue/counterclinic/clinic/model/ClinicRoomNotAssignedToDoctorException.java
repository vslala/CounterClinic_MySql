package com.codesvenue.counterclinic.clinic.model;

public class ClinicRoomNotAssignedToDoctorException extends RuntimeException {
    public ClinicRoomNotAssignedToDoctorException(String msg) {
        super(msg);
    }
}
