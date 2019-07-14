package com.codesvenue.counterclinic.clinic.dao;

import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.walkinappointment.TestData;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;

public class FakeClinicRepository implements ClinicRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public FakeClinicRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<ClinicRoom> fetchClinics() {
        return TestData.getClinics();
    }

    @Override
    public Integer createNewClinicRoom(Integer clinicId, String name) {
        return 1;
    }

    @Override
    public User assignClinicRoom(User doctor) {
        return doctor;
    }
}
