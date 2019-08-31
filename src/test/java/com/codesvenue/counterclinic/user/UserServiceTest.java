package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.model.Clinic;
import com.codesvenue.counterclinic.clinic.model.ClinicForm;
import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.clinic.model.Setting;
import com.codesvenue.counterclinic.user.dao.UserRepository;
import com.codesvenue.counterclinic.user.dao.UserRepositoryMySql;
import com.codesvenue.counterclinic.user.model.PreferredLanguage;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserLogin;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.user.service.UserService;
import com.codesvenue.counterclinic.user.service.UserServiceImpl;
import com.codesvenue.counterclinic.walkinappointment.TestData;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentInfoForm;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Before
    public void setup(){
//        Mockito mock = new Mockito();
//        UserRepository userRepository = new FakeUserRepository();
//        UserRepository origUserRepository = new UserRepositoryMySql(TestData.getNamedParameterJdbcTemplate());
//        SimpMessagingTemplate simpMessagingTemplate =  mock.mock(SimpMessagingTemplate.class);
//        userService = new UserServiceImpl(origUserRepository, simpMessagingTemplate);
//        ReflectionTestUtils.setField(userService, "qrCodeFolder", "src/test/resources/static/qrcode");
//        ReflectionTestUtils.setField(userService, "qrCodeUrlPath", "qrcode");
    }

    @Ignore("Not part of walkin api anymore. Moved to Online Api")
    @Test
    public void itShouldRegisterNewUser() {
        User user = User.newInstance().firstName("Sachin").lastName("Tendulkar")
                .email("sachin.tendulkar@gmail.com").mobile("8888999912").preferredLanguage(PreferredLanguage.ENGLISH)
                .username("sachin").roles(UserRole.ADMIN);
        user.setPassword("simplepass");
        UserLogin userLogin = userService.createNewUser(user);
        Assert.assertNotNull(userLogin);
        Assert.assertNotNull(userLogin.getUserId());
        Assert.assertEquals("sachin", userLogin.getUsername());
    }

    @Test
    public void itShouldCascadeDeleteUser() {
        Integer userId = 1;
        boolean rowsAffected = userService.deleteUser(userId);
        Assert.assertTrue(rowsAffected);
    }

    @Test
    public void itShouldCreateNewClinic() {
        User admin = new User();
        admin.setRoles(Arrays.asList(UserRole.ADMIN));

        ClinicForm clinicForm = new ClinicForm();
        clinicForm.setClinicName("TestClinic");
        Clinic clinic = userService.createNewClinic(admin, clinicForm);
        Assert.assertNotNull(clinic);
    }

    @Test
    public void itShouldAssignClinicRoomToDoctor() {
        // Arrange
        User receptionist = TestData.getNewUser(UserRole.RECEPTIONIST);
        Integer assignDoctorId = userService.createNewUser(receptionist).getUserId();
        ClinicForm clinicForm = new ClinicForm();
        clinicForm.setClinicName("Test Clinic");
        Integer clinicRoomId = userService.createNewClinic(TestData.getNewUser(UserRole.ADMIN), clinicForm).getClinicId();

        // Act
        User doctor = userService.assignDoctorClinic(receptionist, clinicRoomId, assignDoctorId);

        // Asserts
        Assert.assertNotNull(doctor);
        Assert.assertNotNull(doctor.getClinicRoomId());
    }

    @Test
    public void doctorCallsNextPatient() {
        User doctor = new User();
        doctor.setRoles(Arrays.asList(UserRole.DOCTOR));
        doctor.setClinicRoom(ClinicRoom.newInstance("X-RAY"));
        AppointmentStatus appointmentStatus = new AppointmentStatus().appointmentStatusId(1);
        appointmentStatus.setCurrentAppointmentId(1);

        WalkInAppointment nextAppointment = userService.notifyReceptionToSendNextPatient(doctor, appointmentStatus);

        Assert.assertNotNull(nextAppointment);
    }

    @Test
    public void createNewDoctor() {
        User admin = TestData.getNewUser(UserRole.ADMIN);
        User doctor = TestData.getNewUser(UserRole.DOCTOR);
        User newDoctor = userService.addNewDoctor(admin, doctor);
        Assert.assertNotNull(newDoctor);
        Assert.assertNotNull(newDoctor.getUserId());
    }

    @Test
    public void itShouldFetchAllTheUserByRole() {
        // Arrange
        User user = TestData.getNewUser(UserRole.DOCTOR);
        userService.createNewUser(user);

        // Act
        List<User> doctors = userService.getAllUsers(UserRole.DOCTOR);

        // Asserts
        Assert.assertNotNull(doctors);
        Assert.assertFalse(doctors.isEmpty());
    }

    @Test
    public void itShouldFetchUserById() {
        // Arrange
        UserLogin userLogin = userService.createNewUser(TestData.getNewUser(UserRole.DOCTOR));

        // Act
        User user = userService.getUser(userLogin.getUserId());

        // Asserts
        Assert.assertNotNull(user);
    }

    @Test
    public void itShouldGetAllUsers() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        SimpMessagingTemplate simpMessagingTemplate = mock(SimpMessagingTemplate.class);
        userService = new UserServiceImpl(userRepository, simpMessagingTemplate);
        List<User> userList = new ArrayList<>();
        userList.add(TestData.getNewUser(UserRole.ADMIN));
        when(userRepository.findAllUsers()).thenReturn(userList);

        // Act
        List<User> users = userService.getAllUsers();

        // Asserts
        Assert.assertFalse(users.isEmpty());
    }

    @Test
    public void itShouldUpdateUserDetails() {
        User user = TestData.getNewUser(UserRole.DOCTOR);
        user.setUserId(1);
        user.setFirstName("FirstName");
        User updatedUser = userService.updateUser(user);
        Assert.assertNotNull(updatedUser);
        Assert.assertEquals("FirstName", updatedUser.getFirstName());
    }

    @Test
    public void itShouldUploadFileAndStoreTheInfoIntoDatabaseSettingTable() throws IOException {
        // Arrange
        String folderPath = "src/test/resources/static/images";
        deleteDirectory(folderPath);
        MockMultipartFile file = new MockMultipartFile("lala", "lala.jpg", "image/jpg", Files.readAllBytes(Paths.get("src/test/resources/lala.jpg")));

        // Act
        Setting uploadedFile = userService.uploadFile(file, "Test");

        // Asserts
        Assert.assertNotNull(uploadedFile);
        Assert.assertEquals("Test", uploadedFile.getSettingName());
        Assert.assertEquals("images/lala.jpg", uploadedFile.getSettingValue());
    }

    private void deleteDirectory(String folderPath) {
        File file = Paths.get(folderPath).toFile();
        if (!file.isDirectory()) {
            file.delete();
            return;
        }

        for (File f : file.listFiles()) {
            f.delete();
        }
        file.delete();
    }

    @Test
    public void itShouldSanitizeTheImageNameBeforeUploading() throws IOException {
        // Arrange
        String folderPath = "src/test/resources/static/images";
        deleteDirectory(folderPath);
        String filePath = "src/test/resources/Your Life Has No Purpose, Or Does It_.png";
        MockMultipartFile file = new MockMultipartFile("TestAttachmentType", "Your Life Has No Purpose, Or Does It_.png", "image/png", Files.readAllBytes(Paths.get(filePath)));

        // Act
        Setting uploadedFile = userService.uploadFile(file, "TestAttachmentType");

        // Asserts
        Assert.assertNotNull(uploadedFile);
        Assert.assertEquals("TestAttachmentType", uploadedFile.getSettingName());
        Assert.assertEquals("images/YourLifeHasNoPurposeOrDoesIt.png", uploadedFile.getSettingValue());
    }

    @Test
    public void itShouldFetchSettingByName() {
        // Arrange
        userService.updateSetting(new Setting("testSettingKey", "testSettingValue"));

        // Act
        Setting setting = userService.getSetting("testSettingKey");

        // Asserts
        Assert.assertNotNull(setting);
        Assert.assertEquals("testSettingKey", setting.getSettingName());
        Assert.assertEquals("testSettingValue", setting.getSettingValue());
    }

    @Test
    public void itShouldFetchAllSettings() {
        List<Setting> settings = userService.getSettings();
        Assert.assertNotNull(settings);
        Assert.assertFalse(settings.isEmpty());
    }

    @Test
    public void itShouldUpdateSetting() {
        Setting setting = Setting.newInstance("TestSetting", "Test Update Setting");
        Setting updatedSetting = userService.updateSetting(setting);
        Assert.assertEquals("Test Update Setting", updatedSetting.getSettingValue());
    }

    @Test
    public void itShouldDeleteSettingById() {
        Integer id = 1;
        Boolean isSuccess = userService.deleteSetting(id);
        Assert.assertTrue(isSuccess);
    }
}
