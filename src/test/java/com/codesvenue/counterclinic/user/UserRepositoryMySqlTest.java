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

    @Test
    public void insertNewUserToDatabase(){
        User user = User.newInstance()
                .firstName("Varun").lastName("Shrivastava")
                .email("varunshrivastava007@gmail.com")
                .mobile("9960543885")
                .username("vslala")
                .preferredLanguage(PreferredLanguage.ENGLISH);
        user.setPassword("simplepass"); // set password to hash it

        User newUser = userRepository.createNewUser(user);
        Assert.assertNotNull(newUser);
        Assert.assertNotNull(newUser.getUserId());
        Assert.assertFalse(newUser.getUserId() == 0);
    }

    @Test
    public void insertUserLoginDetailIntoTheDatabase() {
        UserLogin userLogin = new UserLogin(1, "vslala", "simplepass");
        UserLogin newUserLogin = userRepository.createNewUserLogin(userLogin);
        Assert.assertNotNull(newUserLogin);
        Assert.assertNotNull(newUserLogin.getId());
    }
}
