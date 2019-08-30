package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.walkinappointment.dao.AppointmentRepository;
import com.codesvenue.counterclinic.walkinappointment.dao.NoMoreAppointmentsException;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentStatusTest {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Before
    public void setup() {
        TestData.appointmentStatusList.add(TestData.firstPersonGoesInsideTheDoctorCabin());
        TestData.appointmentStatusList.add(TestData.secondPersonGoesInsideTheDoctorCabin());
    }

    @Test
    public void generateAppointmentStatus() {
        // Arrange
        List<WalkInAppointment> walkInAppointmentList = appointmentRepository.fetchDoctorAppointmentsForToday(1);
        AppointmentStatus appointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorIdForToday(1).get();
        LocalDateTime creationTime = LocalDateTime.now();

        // Act
        AppointmentStatus newAppointmentStatus = appointmentStatus.generateAppointmentStatus(walkInAppointmentList.get(0), creationTime.plusMinutes(15));

        // Asserts
        Assert.assertNotNull(newAppointmentStatus);
        Assert.assertEquals(1, (int)newAppointmentStatus.getCurrentAppointmentId());
        Assert.assertEquals(15, (int)newAppointmentStatus.getAvgWaitingTime());
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Ignore
    @Test
    public void itShouldThrowNoMoreAppointmentsException() {
        expectedException.expect(NoMoreAppointmentsException.class);
        List<WalkInAppointment> walkInAppointmentList = TestData.buildWalkInAppointments(2, 15);

        AppointmentStatus appointmentStatus = TestData.appointmentStatusList.get(TestData.appointmentStatusList.size()-1);

        LocalDateTime creationTime = LocalDateTime.of(LocalDate.of(2019, Month.AUGUST, 025), LocalTime.of(11, 30));
        AppointmentStatus newAppointmentStatus = appointmentStatus.generateAppointmentStatus(walkInAppointmentList.get(0), creationTime);
        Assert.assertNotNull(newAppointmentStatus);
        Assert.assertEquals(3, (int)newAppointmentStatus.getCurrentAppointmentId());
        Assert.assertEquals(15, (int)newAppointmentStatus.getAvgWaitingTime());
    }

    @Test
    public void itShouldUpdateTheAvgTimeWhenDoctorIsOnTheBreak() {
        // Arrange
        AppointmentStatus appointmentStatus = appointmentRepository.fetchAppointmentStatusListForToday(1).get(0);
        Integer breakTime = 45;

        // Act
        AppointmentStatus newAppointmentStatus = appointmentStatus.calculateNewAvgWaitTime(breakTime);

        // Asserts
        Assert.assertNotNull(newAppointmentStatus);
        Assert.assertEquals(60, (int)newAppointmentStatus.getAvgWaitingTime());
    }
}
