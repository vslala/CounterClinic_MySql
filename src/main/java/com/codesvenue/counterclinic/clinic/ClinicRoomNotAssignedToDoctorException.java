package com.codesvenue.counterclinic.clinic;

public class ClinicRoomNotAssignedToDoctorException extends RuntimeException {
    public ClinicRoomNotAssignedToDoctorException(String msg) {
        super(msg);
    }
}
