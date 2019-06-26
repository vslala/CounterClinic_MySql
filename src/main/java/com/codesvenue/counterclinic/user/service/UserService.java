package com.codesvenue.counterclinic.user.service;

import com.codesvenue.counterclinic.clinic.model.Clinic;
import com.codesvenue.counterclinic.clinic.model.ClinicForm;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentInfoForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    Clinic createNewClinic(User admin, ClinicForm clinicForm);

    WalkInAppointment createNewWalkInAppointment(User receptionist, WalkInAppointmentInfoForm walkInAppointmentInfoForm);

    WalkInAppointment notifyReceptionToSendNextPatient(User doctor, AppointmentStatus nextAppointmentId);

    User addNewDoctor(User admin, User doctor);

    User assignDoctorClinic(User receptionist, Integer clinicForm, Integer assignDoctorId);

    List<User> getAllUsers(UserRole userRole);

    User getUser(int userId);
}
