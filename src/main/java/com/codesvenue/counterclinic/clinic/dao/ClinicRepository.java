package com.codesvenue.counterclinic.clinic.dao;

import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicRepository {
    List<ClinicRoom> fetchClinics();

    Integer createNewClinicRoom(Integer clinicId, String name);

    User assignClinicRoom(User doctor);
}
