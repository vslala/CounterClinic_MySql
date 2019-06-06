package com.codesvenue.counterclinic.user;

public class ClinicRoomNotAssignedToDoctorException extends RuntimeException {
    public ClinicRoomNotAssignedToDoctorException(String msg) {
        super(msg);
    }
}
