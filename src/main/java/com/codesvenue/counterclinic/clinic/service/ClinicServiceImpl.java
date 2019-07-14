package com.codesvenue.counterclinic.clinic.service;

import com.codesvenue.counterclinic.clinic.dao.ClinicRepository;
import com.codesvenue.counterclinic.clinic.model.AssignClinicForm;
import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.user.model.User;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;

    public ClinicServiceImpl(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    @Override
    public List<ClinicRoom> getAllClinics() {
        return clinicRepository.fetchClinics();
    }

    @Override
    public ClinicRoom createNewClinic(ClinicRoom clinicRoom) {
        Integer lastInsertId = clinicRepository.createNewClinicRoom(clinicRoom.getClinicId(), clinicRoom.getName());
        return ClinicRoom.copyInstance(clinicRoom).clinicRoomId(lastInsertId);
    }

    @Override
    public User assignClinicRoom(AssignClinicForm assignClinicForm) {
        User assignedDoctor = assignClinicForm.getSelectedDoctor().assignClinicRoom(assignClinicForm.getSelectedClinicRoom());
        return clinicRepository.assignClinicRoom(assignedDoctor);
    }
}
