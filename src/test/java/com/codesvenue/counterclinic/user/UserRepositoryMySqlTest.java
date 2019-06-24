package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicRoom;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.qrcode.QRCodeBuilder;
import com.codesvenue.counterclinic.walkinappointment.TestData;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void shouldFindDoctorById() {
        User doctor = userRepository.findDoctorById(1);
        Assert.assertEquals(doctor.getUsername(), "pvrano");
        Assert.assertNotNull(doctor.getClinicRoomId());
        Assert.assertNotNull(doctor.getRoles());
    }

    @Test
    public void itShouldCreateNewWalkInAppointment() {
        WalkInAppointment walkInAppointment = new WalkInAppointment();
        walkInAppointment.setAppointedDoctorId(2);
        walkInAppointment.setPatientFirstName("Test Patient");
        walkInAppointment.setPatientLastName("Test Patient");
        WalkInAppointment newAppointment = userRepository.createNewWalkInAppointment(walkInAppointment);

        Assert.assertNotNull(newAppointment);
        Assert.assertNotNull(newAppointment.getWalkInAppointmentId());
    }

    @Test
    public void shouldFindAppointmentWithId() {
        WalkInAppointment walkInAppointment = userRepository.findAppointmentById(1);
        Assert.assertNotNull(walkInAppointment);
        Assert.assertEquals(1, (int) walkInAppointment.getWalkInAppointmentId());
    }

    @Test
    public void itShouldCreateNewQRCodeIntoTheDatabase() {
        Map<String, Object> qrCodeData = new HashMap<>();
        qrCodeData.put("appointmentId", 1);
        qrCodeData.put("appointedDoctorId", 1);

        long now = System.currentTimeMillis();
        QRCode qrCode = QRCodeBuilder.newInstance()
                .filePath("src/main/resources/static/qrcode/testcode.png").url("qrcode/testcode.png").build(
                1,
                qrCodeData
        );

        QRCode newQRCode = userRepository.createNewQRCode(qrCode);
        Assert.assertNotNull(newQRCode);
        Assert.assertNotNull(newQRCode.getQrCodeId());
    }

    @Test
    public void itShouldFindClinicRoomById() {
        ClinicRoom clinicRoom = userRepository.findClinicRoomById(2);
        Assert.assertNotNull(clinicRoom);
        Assert.assertNotNull(clinicRoom.getClinicRoomId());
    }

    @Test
    public void itShouldUpdateUserMeta() {
        UserMeta userMeta = userRepository.updateUserMeta(1, UserConstants.ASSIGNED_CLINIC_ROOM, "2");
        Assert.assertNotNull(userMeta);
        Assert.assertNotNull(userMeta.getMetaId());
    }

    @Test
    public void itShouldFetchAllTheUsersByRole() {
        List<User> users = userRepository.findAllUsersByRole(UserRole.DOCTOR);
        Assert.assertNotNull(users);
        Assert.assertFalse(users.isEmpty());
    }

    @Test
    public void itShouldFetchUserByUserId() {
        User user = userRepository.findUserById(1);
        Assert.assertNotNull(user);
    }
}
