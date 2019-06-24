package com.codesvenue.counterclinic.walkinappointment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class WalkInAppointmentInfoForm {
    @NotNull
    private String patientFirstName;
    @NotNull
    private String patientLastName;
    private Integer doctorId;
    private Integer clinicId;

    public static WalkInAppointmentInfoForm newInstance() {
        return new WalkInAppointmentInfoForm();
    }

    public WalkInAppointmentInfoForm firstName(String firstName) {
        this.patientFirstName =  firstName;
        return this;
    }

    public WalkInAppointmentInfoForm lastName(String lastName) {
        this.patientLastName = lastName;
        return this;
    }

    public WalkInAppointmentInfoForm appointedDoctorId(int doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public WalkInAppointmentInfoForm clinicId(int clinicId) {
        this.clinicId = clinicId;
        return this;
    }
}
