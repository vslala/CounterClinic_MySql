package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicForm;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointmentInfoForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    Clinic createNewClinic(User admin, ClinicForm clinicForm);

    WalkInAppointment createNewWalkInAppointment(User receptionist, WalkInAppointmentInfoForm walkInAppointmentInfoForm);

    WalkInAppointment notifyReceptionToSendNextPatient(User doctor, Integer nextAppointmentId);

    User addNewDoctor(User admin, User doctor);

    User assignDoctorClinic(User receptionist, Integer clinicForm, Integer assignDoctorId);

    List<User> getAllUsers(UserRole userRole);

    User getUser(int userId);
}
