package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.clinic.model.ClinicRoomNotAssignedToDoctorException;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.walkinappointment.model.AppointedUserIsNotADoctor;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

public class WalkInAppointmentTest {

    private WalkInAppointment walkInAppointment;

    String firstName  = "Patient First Name";
    String lastName = "Patient Last Name";
    User appointedDoctor = new User();

    @Before
    public void setup() {
        walkInAppointment = new WalkInAppointment();
        appointedDoctor.setRoles(Arrays.asList(UserRole.DOCTOR));
        appointedDoctor.setClinicRoom(ClinicRoom.newInstance("X-RAY"));
    }

    @Rule
    public ExpectedException  expectedException = ExpectedException.none();

    @Test
    public void itShouldThrowClinicRoomNotAssignedToDoctorException() {
        expectedException.expect(ClinicRoomNotAssignedToDoctorException.class);

        appointedDoctor.setRoles(Arrays.asList(UserRole.DOCTOR));

        WalkInAppointment newWalkInAppointment = walkInAppointment.create(firstName, lastName, appointedDoctor);
    }

    @Test
    public void itShouldThrowAppointedUserIsNotADoctorException() {
        expectedException.expect(AppointedUserIsNotADoctor.class);

        appointedDoctor.setRoles(Arrays.asList(UserRole.ADMIN));

        WalkInAppointment newWalkInAppointment = walkInAppointment.create(firstName, lastName, appointedDoctor);
    }

    @Test
    public void itShouldCreateNewAppointment() {
        WalkInAppointment newWalkInAppointment = walkInAppointment.create(firstName, lastName, appointedDoctor);
        System.out.println(newWalkInAppointment.getCreatedAt());

        Assert.assertNotNull(newWalkInAppointment);
        Assert.assertNotNull(newWalkInAppointment.getCreatedAt());
    }

}
