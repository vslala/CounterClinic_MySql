package com.codesvenue.counterclinic.clinic;

import com.codesvenue.counterclinic.clinic.dao.ClinicRepository;
import com.codesvenue.counterclinic.clinic.dao.ClinicRepositoryMySql;
import com.codesvenue.counterclinic.clinic.dao.FakeClinicRepository;
import com.codesvenue.counterclinic.clinic.model.AssignClinicForm;
import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.clinic.service.ClinicService;
import com.codesvenue.counterclinic.clinic.service.ClinicServiceImpl;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.walkinappointment.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ClinicServiceTest {

    private ClinicService clinicService;

    @Before
    public void setup() {
//        ClinicRepository clinicRepository = new FakeClinicRepository(TestData.getNamedParameterJdbcTemplate());
        ClinicRepository clinicRepository = new ClinicRepositoryMySql(TestData.getNamedParameterJdbcTemplate());
        clinicService = new ClinicServiceImpl(clinicRepository);
    }

    @Test
    public void itShouldCreateNewClinicRoomInDatabase() {
        ClinicRoom clinicRoom = ClinicRoom.newInstanceWithClinicId(1, "X-RAY");
        ClinicRoom newClinicRoom = clinicService.createNewClinic(clinicRoom);
        Assert.assertNotNull(newClinicRoom);
        Assert.assertNotNull(newClinicRoom.getClinicRoomId());
    }

    @Test
    public void itShouldFetchAllTheAvailableClinicsFromTheDatabase() {
        List<ClinicRoom> clinics = clinicService.getAllClinics();
        Assert.assertNotNull(clinics);
        Assert.assertFalse(clinics.isEmpty());
    }

    @Test
    public void itShouldAssignClinicToDoctor() {
        User doctor = User.newInstance().userId(21).username("pvrano");
        ClinicRoom clinicRoom = ClinicRoom.newInstanceWithClinicId(1, "X-RAY").clinicRoomId(1);
        User assignedClinicUser = clinicService.assignClinicRoom(AssignClinicForm.newInstance().selectedDoctor(doctor).selectedClinicRoom(clinicRoom));
        Assert.assertNotNull(assignedClinicUser);
        Assert.assertNotNull(assignedClinicUser.getClinicRoomId());
    }
}
