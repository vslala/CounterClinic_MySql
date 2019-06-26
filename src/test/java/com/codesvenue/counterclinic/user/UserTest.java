package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.model.Clinic;
import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.user.model.ActionNotAllowedException;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.walkinappointment.TestData;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;

public class UserTest {

    private User user;

    @Before
    public void setup() {
        user = new User();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void itShouldThrowActionNotAllowedExceptionIfTheUserIsNotSuperAdmin()  {
        expectedException.expect(ActionNotAllowedException.class);
        user.setRoles(Arrays.asList(UserRole.ADMIN));
        String clinicName = "Counter Clinic";
        Clinic newClinic = user.createNewClinic(clinicName);
    }

    @Test
    public void onlySuperAdminUserCanCreateNewClinics() {
        user.setRoles(Arrays.asList(new UserRole[]{UserRole.SUPER_ADMIN}));
        String clinicName = "Counter Clinic";
        Clinic newClinic = user.createNewClinic(clinicName);
        Assert.assertNotNull(newClinic);
        Assert.assertFalse(newClinic.getClinicName().isEmpty());
    }

    @Test
    public void userShouldNotBeAllowedToAddNewRoomToTheClinicWithoutProperPermissions() {
        expectedException.expect(ActionNotAllowedException.class);
        ClinicRoom clinicRoom = ClinicRoom.newInstance("X-RAY");
        Clinic clinic  = Clinic.newInstance("Counter Clinic");
        Clinic newClinicWithRooms = user.addClinicRoomToClinic(clinic, clinicRoom);
    }

    @Test
    public void superAdminUserCanAddNewRoomsToTheClinic() {
        user.setRoles(Arrays.asList(UserRole.SUPER_ADMIN));
        ClinicRoom clinicRoom = ClinicRoom.newInstance("X-RAY");
        Clinic clinic  = Clinic.newInstance("Counter Clinic");
        Clinic newClinicWithRooms = user.addClinicRoomToClinic(clinic, clinicRoom);
        Assert.assertNotNull(newClinicWithRooms);
        Assert.assertFalse(newClinicWithRooms.getClinicRooms().isEmpty());
    }

    @Test
    public void receptionistUserCanAssignRoomsToDoctor() {
        user.setRoles(Arrays.asList(UserRole.RECEPTIONIST));
        ClinicRoom clinicRoom = ClinicRoom.newInstance("X-RAY");
        User doctor = TestData.getNewUser(UserRole.DOCTOR);
        User assignedDoctor = user.assignClinicRoom(clinicRoom, doctor);
        Assert.assertNotNull(assignedDoctor);
    }

    @Test
    public void superAdminUserCanAssignRoomsToDoctor() {
        user.setRoles(Arrays.asList(UserRole.SUPER_ADMIN));
        ClinicRoom clinicRoom = ClinicRoom.newInstance("X-RAY");
        User doctor = TestData.getNewUser(UserRole.DOCTOR);
        User assignedDoctor = user.assignClinicRoom(clinicRoom, doctor);
        Assert.assertNotNull(assignedDoctor);
    }

    @Test
    public void itShouldThrowExceptionIfUnauthorizedUserTriesToAssignClinicRoomToDoctor() {
        expectedException.expect(ActionNotAllowedException.class);
        user.setRoles(Arrays.asList(UserRole.DOCTOR));
        ClinicRoom clinicRoom = ClinicRoom.newInstance("X-RAY");
        User doctor = TestData.getNewUser(UserRole.DOCTOR);
        User assignedDoctor = user.assignClinicRoom(clinicRoom, doctor);
        Assert.assertNotNull(assignedDoctor);
    }

    @Test
    public void receptionUserCanCreateNewWalkInAppointmentForThePatientsByAppointedDoctor() {
        user.setRoles(Arrays.asList(UserRole.RECEPTIONIST));
        String patientFirstName = "Patient First Name";
        String patientLastName  = "Patient Last Name";
        User appointedDoctor = new User();
        appointedDoctor.setRoles(Arrays.asList(UserRole.DOCTOR));
        appointedDoctor.setClinicRoom(ClinicRoom.newInstance("ClinicRoom"));

        WalkInAppointment walkInAppointment = user.createWalkInAppointment(patientFirstName, patientLastName, appointedDoctor);
        Assert.assertNotNull(walkInAppointment);
    }

    @Test
    public void doctorCallsTheNextPatientInsideAndNotifiesReceptionist() {
        user.setRoles(Arrays.asList(UserRole.DOCTOR));

        AppointmentStatus appointmentStatus = new AppointmentStatus();
        appointmentStatus.setCurrentAppointmentId(2);
        Mockito mock = new Mockito();
        SimpMessagingTemplate simpMessagingTemplate = mock.mock(SimpMessagingTemplate.class);

        boolean isNotified = user.askReceptionistToSendNextPatient(appointmentStatus, simpMessagingTemplate);
        Assert.assertTrue(isNotified);
    }
}
