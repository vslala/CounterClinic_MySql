package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicRoom;
import com.codesvenue.counterclinic.walkinappointment.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserRepositoryMySqlTest {

    private UserRepository userRepository;

    @Before
    public void setup() {
        userRepository = new UserRepositoryMySql(TestData.getNamedParameterJdbcTemplate());
    }

    @Test
    public void createNewClinic() {
        Clinic newClinic = Clinic.newInstance("TestClinic")
                .addNewRoom(ClinicRoom.newInstance("TestClinicRoom-A"))
                .addNewRoom(ClinicRoom.newInstance("TestClinicRoom-B"));
        Clinic clinic = userRepository.createNewClinic(newClinic);
        Assert.assertNotNull(newClinic);
        Assert.assertTrue(clinic.getClinicId() > 0);
    }

    @Test
    public void addNewRoomToClinic() {
        Clinic newClinic = Clinic.newInstance("TestClinic")
                .addNewRoom(ClinicRoom.newInstance("TestClinicRoom-A"))
                .addNewRoom(ClinicRoom.newInstance("TestClinicRoom-B"));
        newClinic.setClinicId(1);
        Clinic clinic = userRepository.createNewClinicRoom(newClinic);
        Assert.assertNotNull(newClinic);
        Assert.assertTrue(clinic.getClinicId() > 0);
        Assert.assertTrue(clinic.getRooms().size() == 2);
    }
}
