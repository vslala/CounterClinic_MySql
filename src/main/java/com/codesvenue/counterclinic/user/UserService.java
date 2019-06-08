package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicForm;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointmentInfoForm;

public interface UserService {
    Clinic createNewClinic(User admin, ClinicForm clinicForm);

    WalkInAppointment createNewWalkInAppointment(User receptionist, WalkInAppointmentInfoForm walkInAppointmentInfoForm);

    WalkInAppointment notifyReceptionToSendNextPatient(User doctor, Integer nextAppointmentId);
}
