package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.user.User;
import com.codesvenue.counterclinic.user.UserRole;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestData {

    public static final String MYSQL_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static List<AppointmentStatus> appointmentStatusList = new ArrayList<>();

    public static List<WalkInAppointment> buildWalkInAppointments(final int count, final int timeDifference) {
        int diff = 0;
        List<WalkInAppointment> walkInAppointments = new LinkedList<>();
        for (int index = 1; index <= count; index++) {
            diff = diff + timeDifference;
            WalkInAppointment walkInAppointment = new WalkInAppointment();
            walkInAppointment.setWalkInAppointmentId(index);
            walkInAppointment.setPatientFirstName(UUID.randomUUID().toString());
            walkInAppointment.setPatientLastName(UUID.randomUUID().toString());
            walkInAppointment.setCreatedAt(now(diff));
            walkInAppointment.setAppointedDoctor(User.newInstance().roles(UserRole.DOCTOR).userId(1));
//            System.out.println(walkInAppointment);
            walkInAppointments.add(walkInAppointment);
        }
        return walkInAppointments;
    }

    private static String now(int timeDifference) {
        return LocalDateTime.now(ZoneOffset.UTC)
                .plus(timeDifference, ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern(MYSQL_DATETIME_PATTERN));
    }

    public static AppointmentStatus firstPersonGoesInsideTheDoctorCabin() {
        AppointmentStatus appointmentStatus1 = new AppointmentStatus();
        appointmentStatus1.setDoctorId(1);
        appointmentStatus1.setCurrentAppointmentId(1);
        appointmentStatus1.setAvgWaitingTime(15);
        appointmentStatus1.setAppointmentStartTime(LocalDateTime.of(LocalDate.of(2019, Month.JUNE, 6), LocalTime.of(11, 00)).format(DateTimeFormatter.ofPattern(TestData.MYSQL_DATETIME_PATTERN)));
        appointmentStatus1.setAppointmentStatusId(1);
        return appointmentStatus1;
    }

    public static AppointmentStatus secondPersonGoesInsideTheDoctorCabin() {
        AppointmentStatus appointmentStatus2 = new AppointmentStatus();
        appointmentStatus2.setDoctorId(1);
        appointmentStatus2.setCurrentAppointmentId(2);
        appointmentStatus2.setAvgWaitingTime(15);
        appointmentStatus2.setAppointmentStartTime(LocalDateTime.of(LocalDate.of(2019, Month.JUNE, 6), LocalTime.of(11, 15)).format(DateTimeFormatter.ofPattern(TestData.MYSQL_DATETIME_PATTERN)));
        appointmentStatus2.setAppointmentStatusId(1);
        return appointmentStatus2;
    }
}
