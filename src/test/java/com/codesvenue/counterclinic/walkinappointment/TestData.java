package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.user.PreferredLanguage;
import com.codesvenue.counterclinic.user.User;
import com.codesvenue.counterclinic.user.UserRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TestData {

    private static final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost/counterclinicdb?serverTimezone=UTC";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";

    public static DriverManagerDataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    public static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    public static final String MYSQL_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static List<AppointmentStatus> appointmentStatusList = new ArrayList<>();

    public static AppointmentStatus store(AppointmentStatus appointmentStatus) {

        if (appointmentStatusList.isEmpty()) {
            appointmentStatus.setAppointmentStatusId(1);
            appointmentStatus.setPatientsInVisitedQueue(appointmentStatus.getPatientsInVisitedQueue()+1);
            appointmentStatusList.add(appointmentStatus);
            return appointmentStatus;
        }
        AppointmentStatus lastAppointmentStatus = appointmentStatusList.get(appointmentStatusList.size()-1);
        appointmentStatus.setPatientsInVisitedQueue(lastAppointmentStatus.getPatientsInVisitedQueue()+1);
        appointmentStatusList.add(appointmentStatus);
        return appointmentStatus;
    }

    public static String getUniqStr(int length) {
        String uniqStr = UUID.randomUUID().toString()
                .replaceAll("-", "");
        int maxLength = uniqStr.length();
        if (length > maxLength)
            length  = maxLength;
        return uniqStr.substring(0, length);
    }

    public static User getNewUser(UserRole... roles) {
        User user = User.newInstance()
                .firstName(getUniqStr(5))
                .lastName(getUniqStr(10))
                .email(getUniqStr(6).concat("@gmail.com"))
                .mobile(getUniqStr(10))
                .username(getUniqStr(5))
                .preferredLanguage(PreferredLanguage.ENGLISH)
                .roles(roles);
        user.setPassword("simplepass");
        return user;
    }

    public static AppointmentStatus remove(AppointmentStatus appointmentStatus) {
        int index = 0;
        for (AppointmentStatus status : appointmentStatusList) {
            if (status.getCurrentAppointmentId().equals(appointmentStatus.getCurrentAppointmentId()))
                break;
            index ++;
        }
        return appointmentStatusList.remove(index);
    }

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
            walkInAppointment.setAppointedDoctorId(1);
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
        return appointmentStatus1;
    }

    public static AppointmentStatus secondPersonGoesInsideTheDoctorCabin() {
        AppointmentStatus appointmentStatus2 = new AppointmentStatus();
        appointmentStatus2.setDoctorId(1);
        appointmentStatus2.setCurrentAppointmentId(2);
        appointmentStatus2.setAvgWaitingTime(15);
        appointmentStatus2.setAppointmentStartTime(LocalDateTime.of(LocalDate.of(2019, Month.JUNE, 6), LocalTime.of(11, 15)).format(DateTimeFormatter.ofPattern(TestData.MYSQL_DATETIME_PATTERN)));
        return appointmentStatus2;
    }

    public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(getDataSource());
    }
}
