package com.codesvenue.counterclinic.walkinappointment;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Log4j
public class WalkInAppointmentServiceImpl implements WalkInAppointmentService {
    private static final String MYSQL_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private final AppointmentRepository appointmentRepository;

    public WalkInAppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentStatus getAppointmentStatus(Integer appointmentId, Integer doctorId, LocalDateTime inquiryTime) {
        List<WalkInAppointment> walkInAppointmentList = appointmentRepository.fetchDoctorAppointmentsForToday(doctorId);
        List<AppointmentStatus> appointmentStatusList = appointmentRepository.fetchAppointmentStatusList(doctorId);
        int patientsBeforeThisAppointmentId = findPatientsBeforeGivenAppointmentId(appointmentId, walkInAppointmentList);

        if (appointmentStatusList.isEmpty()) {
            // doctor has not started taking patients
            return buildAppointmentStatusFirstTime(appointmentId, doctorId, patientsBeforeThisAppointmentId);
        }

        int approxAvgWaitTime = calculateAvgWaitingTime(inquiryTime, appointmentStatusList, patientsBeforeThisAppointmentId);

        return buildAppointmentStatus(appointmentId, doctorId, inquiryTime, approxAvgWaitTime);
    }

    private AppointmentStatus buildAppointmentStatus(Integer appointmentId, Integer doctorId, LocalDateTime inquiryTime, int approxAvgWaitTime) {
        AppointmentStatus appointmentStatus = new AppointmentStatus();
        appointmentStatus.setCurrentAppointmentId(appointmentId);
        appointmentStatus.setAvgWaitingTime(approxAvgWaitTime);
        appointmentStatus.setAppointmentStartTime(inquiryTime.plus(approxAvgWaitTime, ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern(MYSQL_DATETIME_PATTERN)));
        appointmentStatus.setDoctorId(doctorId);
        return appointmentStatus;
    }

    private int calculateAvgWaitingTime(LocalDateTime inquiryTime, List<AppointmentStatus> appointmentStatusList, int patientsBeforeThisAppointmentId) {
        int patientsInVisitedQueue = appointmentStatusList.size();
        AppointmentStatus appointmentStatus = appointmentStatusList.get(patientsInVisitedQueue - 1);

        int minutesPassed = (int)LocalDateTime.parse(
                appointmentStatus.getAppointmentStartTime(), DateTimeFormatter.ofPattern(MYSQL_DATETIME_PATTERN))
                .until(inquiryTime, ChronoUnit.MINUTES);
        int appointmentsRemainingBeforeThisAppointment = patientsBeforeThisAppointmentId - patientsInVisitedQueue;
        int avgWaitingTimeTillCurrentAppointment = appointmentStatus.getAvgWaitingTime();
        int newAvgWaitingTime = (avgWaitingTimeTillCurrentAppointment + minutesPassed) / 2;
        int approxAvgWaitTime = (newAvgWaitingTime * (appointmentsRemainingBeforeThisAppointment)) + minutesPassed
                + appointmentStatus.getDoctorBreakTime();

        System.out.println("Patients before this appointment: " + patientsBeforeThisAppointmentId);
        System.out.println("Minutes passed from current appointment start time to inquiry time: " + minutesPassed);
        System.out.println("Patients in the visited queue: " + patientsInVisitedQueue);
        System.out.println("Patients remaining to be checked before this: " + appointmentsRemainingBeforeThisAppointment);
        System.out.println("Avg waiting time till current appointment: " + avgWaitingTimeTillCurrentAppointment);
        System.out.println("New avg waiting time: " + newAvgWaitingTime);
        System.out.println("Approx. Avg. Waiting time for this appointment: " + approxAvgWaitTime);
        System.out.println("Doctor Break Time: " + appointmentStatus.getDoctorBreakTime());
        return approxAvgWaitTime;
    }

    private LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    private int findPatientsBeforeGivenAppointmentId(Integer appointmentId, List<WalkInAppointment> walkInAppointmentList) {
        int patientsBeforeThisAppointmentId = 0;
        for (WalkInAppointment walkInAppointment: walkInAppointmentList) {
            if (walkInAppointment.getWalkInAppointmentId().equals(appointmentId)) {
                break;
            }
            patientsBeforeThisAppointmentId++;
        }
        return patientsBeforeThisAppointmentId;
    }

    private AppointmentStatus buildAppointmentStatusFirstTime(Integer appointmentId, Integer doctorId, int patientsBeforeThisAppointmentId) {
        AppointmentStatus appointmentStatus = new AppointmentStatus();
        appointmentStatus.setAvgWaitingTime(15 * patientsBeforeThisAppointmentId);
        appointmentStatus.setCurrentAppointmentId(appointmentId);
        appointmentStatus.setDoctorId(doctorId);
        return appointmentStatus;
    }

    /*
    WalkInAppointment currentWalkInAppointment = walkInAppointmentList.get(0);
            AppointmentStatus appointmentStatus = new AppointmentStatus();
            appointmentStatus.setDoctorId(currentWalkInAppointment.getAppointedDoctor().getUserId());
            appointmentStatus.setCurrentAppointmentId(currentWalkInAppointment.getWalkInAppointmentId());
            appointmentStatus.setAppointmentStartTime(
                    LocalDateTime.now(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ISO_DATE_TIME)
                            .replace("T", " "));
            appointmentStatus.setAvgWaitingTime(15);
            return appointmentRepository.saveAppointmentStatus(appointmentStatus);
     */
}
