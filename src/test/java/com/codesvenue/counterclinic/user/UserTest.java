package com.codesvenue.counterclinic.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
        user.setRoles(Arrays.asList(new UserRole[]{UserRole.SUPERADMIN}));
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
        user.setRoles(Arrays.asList(UserRole.SUPERADMIN));
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
        boolean clinicAssigned = user.assignClinicRoom(clinicRoom);
        Assert.assertTrue(clinicAssigned);
    }

    @Test
    public void superAdminUserCanAssignRoomsToDoctor() {
        user.setRoles(Arrays.asList(UserRole.SUPERADMIN));
        ClinicRoom clinicRoom = ClinicRoom.newInstance("X-RAY");
        boolean clinicAssigned = user.assignClinicRoom(clinicRoom);
        Assert.assertTrue(clinicAssigned);
    }

    @Test
    public void itShouldThrowExceptionIfUnauthorizedUserTriesToAssignClinicRoomToDoctor() {
        expectedException.expect(ActionNotAllowedException.class);
        user.setRoles(Arrays.asList(UserRole.DOCTOR));
        ClinicRoom clinicRoom = ClinicRoom.newInstance("X-RAY");
        boolean clinicAssigned = user.assignClinicRoom(clinicRoom);
        Assert.assertTrue(clinicAssigned);
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
}
