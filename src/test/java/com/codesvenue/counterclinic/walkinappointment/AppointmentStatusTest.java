package com.codesvenue.counterclinic.walkinappointment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AppointmentStatusTest {

    @Before
    public void setup() {
    }

    @Test
    public void generateAppointmentStatus() {
        List<WalkInAppointment> walkInAppointmentList = TestData.buildWalkInAppointments(10, 15);

        TestData.appointmentStatusList.add(TestData.firstPersonGoesInsideTheDoctorCabin());
        TestData.appointmentStatusList.add(TestData.secondPersonGoesInsideTheDoctorCabin());

        List<AppointmentStatus> appointmentStatusList = TestData.appointmentStatusList;

        AppointmentStatus appointmentStatus = TestData.appointmentStatusList.get(TestData.appointmentStatusList.size()-1);
        AppointmentStatus newAppointmentStatus = appointmentStatus.generateAppointmentStatus(walkInAppointmentList);
        Assert.assertNotNull(newAppointmentStatus);
    }
}
