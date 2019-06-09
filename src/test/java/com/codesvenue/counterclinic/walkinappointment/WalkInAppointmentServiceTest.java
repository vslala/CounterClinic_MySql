package com.codesvenue.counterclinic.walkinappointment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class WalkInAppointmentServiceTest {

    private WalkInAppointmentService walkInAppointmentService;

    @Before
    public void setup() {
        AppointmentRepository appointmentRepository = new FakeAppointmentRepository();
        walkInAppointmentService = new WalkInAppointmentServiceImpl(appointmentRepository);
        TestData.appointmentStatusList = new ArrayList<>();
    }

    @Test
    public void itShouldFetchTheAvgWaitingTimeOfThePatientAppointment_DoctorHasNotStartedTakingPatient() {
        Integer appointmentId = 5;
        Integer doctorId = 1;

        AppointmentStatus appointmentStatus = walkInAppointmentService.getAppointmentStatus(appointmentId, doctorId, LocalDateTime.now(ZoneOffset.UTC));
        Assert.assertNotNull(appointmentStatus);
        Assert.assertEquals(60, (int) appointmentStatus.getAvgWaitingTime());
    }

    @Test
    public void fetchAvgWaitingTimeWhenThreePatientsHaveBeenChecked() {
        Integer doctorId = 1;
        Integer appointmentId = 5;

        AppointmentStatus appointmentStatus1 = TestData.firstPersonGoesInsideTheDoctorCabin();
        AppointmentStatus appointmentStatus2 = TestData.secondPersonGoesInsideTheDoctorCabin();

        TestData.appointmentStatusList.add(appointmentStatus1);
        TestData.appointmentStatusList.add(appointmentStatus2);

        LocalDateTime inquiryTime = LocalDateTime.of(LocalDate.of(2019, Month.JUNE, 6), LocalTime.of(11, 30));
        AppointmentStatus appointmentStatus = walkInAppointmentService.getAppointmentStatus(appointmentId, doctorId, inquiryTime);
        Assert.assertNotNull(appointmentStatus);
        Assert.assertEquals(45, (int)appointmentStatus.getAvgWaitingTime());
    }

    @Test
    public void itShouldAddTheDoctorBreakTimeToTheAppointmentAvgWaitingTime() {
        Integer doctorId = 1;
        Integer appointmentId = 5;

        AppointmentStatus appointmentStatus1 = TestData.firstPersonGoesInsideTheDoctorCabin();
        AppointmentStatus appointmentStatus2 = TestData.secondPersonGoesInsideTheDoctorCabin();

        TestData.appointmentStatusList.add(appointmentStatus1);
        TestData.appointmentStatusList.add(appointmentStatus2);
        // have to update the doctor break time in the last appointment of the queue
        TestData.appointmentStatusList.get(TestData.appointmentStatusList.size()-1).setDoctorBreakTime(10);

        LocalDateTime inquiryTime = LocalDateTime.of(LocalDate.of(2019, Month.JUNE, 6), LocalTime.of(11, 30));
        AppointmentStatus appointmentStatus = walkInAppointmentService.getAppointmentStatus(appointmentId, doctorId, inquiryTime);
        Assert.assertNotNull(appointmentStatus);
        Assert.assertEquals(55, (int)appointmentStatus.getAvgWaitingTime());
    }
}
