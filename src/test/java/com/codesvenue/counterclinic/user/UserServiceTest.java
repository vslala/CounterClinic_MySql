package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicForm;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointmentInfoForm;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

public class UserServiceTest {

    private UserService userService;

    @Before
    public void setup(){
        UserRepository userRepository = new FakeUserRepository();
        userService = new UserServiceImpl(userRepository);
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
        ReflectionTestUtils.setField(userService, "qrCodeFolder", "src/test/resources/qrcode");
        File file = Paths.get("src/test/resources/qrcode").toFile();
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
}
