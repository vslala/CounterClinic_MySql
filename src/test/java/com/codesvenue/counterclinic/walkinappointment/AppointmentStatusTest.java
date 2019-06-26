package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.walkinappointment.dao.NoMoreAppointmentsException;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

public class AppointmentStatusTest {

    @Before
    public void setup() {
        TestData.appointmentStatusList.add(TestData.firstPersonGoesInsideTheDoctorCabin());
        TestData.appointmentStatusList.add(TestData.secondPersonGoesInsideTheDoctorCabin());
    }

    @Test
    public void generateAppointmentStatus() {
        List<WalkInAppointment> walkInAppointmentList = TestData.buildWalkInAppointments(10, 15);

        AppointmentStatus appointmentStatus = TestData.appointmentStatusList.get(TestData.appointmentStatusList.size()-1);

        LocalDateTime creationTime = LocalDateTime.of(LocalDate.of(2019, Month.JUNE, 06), LocalTime.of(11, 30));
        AppointmentStatus newAppointmentStatus = appointmentStatus.generateAppointmentStatus(walkInAppointmentList.get(0), creationTime);
        Assert.assertNotNull(newAppointmentStatus);
        Assert.assertEquals(3, (int)newAppointmentStatus.getCurrentAppointmentId());
        Assert.assertEquals(15, (int)newAppointmentStatus.getAvgWaitingTime());
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void itShouldThrowNoMoreAppointmentsException() {
        expectedException.expect(NoMoreAppointmentsException.class);
        List<WalkInAppointment> walkInAppointmentList = TestData.buildWalkInAppointments(2, 15);

        AppointmentStatus appointmentStatus = TestData.appointmentStatusList.get(TestData.appointmentStatusList.size()-1);

        LocalDateTime creationTime = LocalDateTime.of(LocalDate.of(2019, Month.JUNE, 06), LocalTime.of(11, 30));
        AppointmentStatus newAppointmentStatus = appointmentStatus.generateAppointmentStatus(walkInAppointmentList.get(0), creationTime);
        Assert.assertNotNull(newAppointmentStatus);
        Assert.assertEquals(3, (int)newAppointmentStatus.getCurrentAppointmentId());
        Assert.assertEquals(15, (int)newAppointmentStatus.getAvgWaitingTime());
    }

    @Test
    public void itShouldUpdateTheAvgTimeWhenDoctorIsOnTheBreak() {
        List<WalkInAppointment> walkInAppointmentList = TestData.buildWalkInAppointments(2, 15);

        AppointmentStatus appointmentStatus = TestData.appointmentStatusList.get(TestData.appointmentStatusList.size()-1);

        LocalDateTime creationTime = LocalDateTime.of(LocalDate.of(2019, Month.JUNE, 06), LocalTime.of(11, 30));
        Integer breakTime = 45;
        AppointmentStatus newAppointmentStatus = appointmentStatus.calculateNewAvgWaitTime(breakTime);
        Assert.assertNotNull(newAppointmentStatus);
        Assert.assertEquals(30, (int)newAppointmentStatus.getAvgWaitingTime());
    }
}
