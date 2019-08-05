package com.codesvenue.counterclinic.walkinappointment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class WalkInAppointmentInfoForm {
    @NotNull
    @NotEmpty
    @Size(min = 3, message = "First Name cannot be less than 3 characters.")
    private String patientFirstName;
    @NotNull
    @NotEmpty
    @Size(min = 3, message = "Last Name cannot be less than 3 characters.")
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
