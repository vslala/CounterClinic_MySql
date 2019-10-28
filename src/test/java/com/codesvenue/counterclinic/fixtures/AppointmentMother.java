package com.codesvenue.counterclinic.fixtures;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AppointmentMother {

    public static final int USER_SLOT_ID = 1;
    public static final float DOCTOR_FEES = 500f;
    public static final String SECRET_KEY = "AZFL7A6wI8ZJjePvyApckCFc2UcluM7fcDNmRHb2QsTmna1RmD0IqJ_O3SJyX";
    public static final String REDIRECT_URL = "www.test.com";
    public static final int APPOINTMENT_ID = 1;
    public static final float CLINIC_TAXES = 5;
    public static final int DOCTOR_ID = 21;
    public static final String START_TIME = "09:00:00";
    public static final String END_TIME = "10:15:00";
    public static final int DURATION = 30;
    public static final int PATIENT_ID = 22;
    public static final LocalDate APPOINTMENT_DATE = LocalDate.of(2019, 07, 17);
    private static final String dbUsername = "root";
    private static final String dbPassword = "P@ssword!";
    private static final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://206.189.30.73/counterclinicdb?serverTimezone=UTC";

//    public static List<Slot> getDoctorAvailableSlots(int slot1Id, int slot2Id) {
//        return getDoctorSlots(slot1Id, slot2Id);
//    }

    public static DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

//    public static List<Slot> getDoctorSlots(int slot1Id, int slot2Id) {
//        List<Slot> availableSlots = new ArrayList<>();
//        Slot slot1 = new Slot();
//        slot1.setSlotId(slot1Id);
//        slot1.setStartTime(LocalTime.of(7, 30));
//        slot1.setEndTime(LocalTime.of(8, 00));
//        Slot slot2 = new Slot();
//        slot2.setSlotId(slot1Id);
//        slot2.setStartTime(LocalTime.of(7, 30));
//        slot2.setEndTime(LocalTime.of(8, 00));
//        availableSlots.add(slot1);
//        availableSlots.add(slot2);
//        return availableSlots;
//    }

//    public static AppointmentForm getInputAppointmentForm() {
//        AppointmentForm apptForm = new AppointmentForm();
//        apptForm.setDoctorId(1);
//        apptForm.setSlotId(1);
//        apptForm.setAppointmentDate(LocalDate.now());
//        return apptForm;
//    }
//
//    public static AppointmentDetails getAppointmentDetails() {
//        return new AppointmentDetails().appointmentDate(LocalDate.now()).doctorId(1).patientId(2).slotId(1);
//    }
//
//    public static Fees getPaymentDetails() {
//        Fees fees = new Fees();
//        fees.setDoctorFees(AppointmentMother.DOCTOR_FEES);
//        fees.setClinicTaxes(AppointmentMother.CLINIC_TAXES);
//        return fees;
//    }
//
//    public static SlotsGenerator getSlotsGenerationInput() {
//        SlotsGenerator input = new SlotsGenerator();
//        input.setDoctorId(DOCTOR_ID);
//        input.setStartTime(START_TIME);
//        input.setStartTime(END_TIME);
//        input.setDurationInMinutes(DURATION);
//        return input;
//    }

    public static Map<String, String> getAppointmentSearchInput(){
        Map<String, String> input = new HashMap<>();
        input.put("online_appointment_id", String.valueOf(APPOINTMENT_ID));
        input.put("doctor_id", String.valueOf(DOCTOR_ID));
        input.put("appointment_date", APPOINTMENT_DATE.toString());
        return input;
    }
}