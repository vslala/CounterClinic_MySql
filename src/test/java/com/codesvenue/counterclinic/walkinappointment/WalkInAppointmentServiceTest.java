package com.codesvenue.counterclinic.walkinappointment;

import org.junit.Before;
import org.junit.Test;

public class WalkInAppointmentServiceTest {

    private WalkInAppointmentService walkInAppointmentService;

    @Before
    public void setup() {
        walkInAppointmentService = new WalkInAppointmentServiceImpl();
    }
}
