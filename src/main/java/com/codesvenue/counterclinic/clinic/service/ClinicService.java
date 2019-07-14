package com.codesvenue.counterclinic.clinic.service;

import com.codesvenue.counterclinic.clinic.model.AssignClinicForm;
import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClinicService {
    List<ClinicRoom> getAllClinics();

    ClinicRoom createNewClinic(ClinicRoom clinicRoom);

    User assignClinicRoom(AssignClinicForm assignClinicForm);
}
