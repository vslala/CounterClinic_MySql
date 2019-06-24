package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicForm;
import com.codesvenue.counterclinic.clinic.ClinicRoom;
import com.codesvenue.counterclinic.walkinappointment.TestData;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointmentInfoForm;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserServiceTest {

    private UserService userService;

    @Before
    public void setup(){
        Mockito mock = new Mockito();
        UserRepository userRepository = new FakeUserRepository();
        UserRepository origUserRepository = new UserRepositoryMySql(TestData.getNamedParameterJdbcTemplate());
        SimpMessagingTemplate simpMessagingTemplate =  mock.mock(SimpMessagingTemplate.class);
        userService = new UserServiceImpl(origUserRepository, simpMessagingTemplate);
        ReflectionTestUtils.setField(userService, "qrCodeFolder", "src/test/resources/static/qrcode");
        ReflectionTestUtils.setField(userService, "qrCodeUrlPath", "qrcode");
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
        User receptionist = TestData.getNewUser(UserRole.RECEPTIONIST);
        Integer assignDoctorId = 1;

        Integer clinicRoomId = 3;

        User doctor = userService.assignDoctorClinic(receptionist, clinicRoomId, assignDoctorId);

        Assert.assertNotNull(doctor);
        Assert.assertNotNull(doctor.getClinicRoomId());
    }

    @Test
    public void itShouldCreateNewAppointment() {
        User receptionist = new User();
        receptionist.setRoles(Arrays.asList(UserRole.RECEPTIONIST));

        WalkInAppointmentInfoForm walkInAppointmentInfoForm = WalkInAppointmentInfoForm.newInstance()
                .appointedDoctorId(1)
                .firstName("Anurag")
                .lastName("Basu");

        WalkInAppointment walkInAppointment = userService.createNewWalkInAppointment(receptionist, walkInAppointmentInfoForm);
        Assert.assertNotNull(walkInAppointment);
        Assert.assertNotNull(walkInAppointment.getWalkInAppointmentId());
    }

    @Test
    public void itShouldCreateNewAppointmentWithQRCodeInOneTransaction() throws IOException {
        // test preparation
        File file = Paths.get("src/test/resources/static/qrcode").toFile();
        FileUtils.deleteDirectory(file);

        // Given
        User receptionist = new User();
        receptionist.setRoles(Arrays.asList(UserRole.RECEPTIONIST));

        WalkInAppointmentInfoForm walkInAppointmentInfoForm = WalkInAppointmentInfoForm.newInstance()
                .appointedDoctorId(1)
                .firstName("Anurag")
                .lastName("Basu");

        // When
        WalkInAppointment walkInAppointment = userService.createNewWalkInAppointment(receptionist, walkInAppointmentInfoForm);

        // Then
        Assert.assertNotNull(walkInAppointment);
        Assert.assertNotNull(walkInAppointment.getWalkInAppointmentId());
        while (true) {
            if (!Objects.isNull(file.listFiles()) && file.listFiles().length > 0)
                break;
        }
        Assert.assertTrue(file.listFiles().length>0);
    }

    @Test
    public void doctorCallsNextPatient() {
        User doctor = new User();
        doctor.setRoles(Arrays.asList(UserRole.DOCTOR));
        doctor.setClinicRoom(ClinicRoom.newInstance("X-RAY"));
        Integer nextAppointmentId  = 2;

        WalkInAppointment nextAppointment = userService.notifyReceptionToSendNextPatient(doctor, nextAppointmentId);

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
        List<User> doctors = userService.getAllUsers(UserRole.DOCTOR);

        Assert.assertNotNull(doctors);
        Assert.assertFalse(doctors.isEmpty());
    }

    @Test
    public void itShouldFetchUserById() {
        User user = userService.getUser(1);
        Assert.assertNotNull(user);
    }
}
