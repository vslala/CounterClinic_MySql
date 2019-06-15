package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.configuration.DateTimeConstants;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Data
@Log4j
public class AppointmentStatus {
    private Integer appointmentStatusId;
    private Integer currentAppointmentId;
    private Integer doctorId;
    private Integer avgWaitingTime = 0;
    private String appointmentStartTime;
    private Integer doctorBreakDuration = 0;
    private Integer patientsInVisitedQueue = 0;

    public static AppointmentStatus newInstanceWithApproxAvgWaitTime(final Integer appointmentId,
                                                                     final int approxAvgWaitTime,
                                                                     final AppointmentStatus lastAppointmentStatus) {
        AppointmentStatus newAppointmentStatus = lastAppointmentStatus;
        newAppointmentStatus.setCurrentAppointmentId(appointmentId);
        newAppointmentStatus.setAvgWaitingTime(approxAvgWaitTime);
        return newAppointmentStatus;
    }

    public AppointmentStatus generateAppointmentStatus(List<WalkInAppointment> walkInAppointmentList, LocalDateTime creationTime) {
        // Fetch the last appointment status from the list
        int appointmentId = this.getCurrentAppointmentId();

        // find the next appointment
        WalkInAppointment nextWalkInAppointment = nextWalkInAppointment(walkInAppointmentList, appointmentId)
                .orElseThrow(() -> new NoMoreAppointmentsException("Doctor has checked all the patients."));

        // calculate new average
        int newAvgWaitTime = calcNewAvgWaitTime(creationTime);

        return buildAppointmentStatus(nextWalkInAppointment, creationTime, newAvgWaitTime);
    }

    private int calcNewAvgWaitTime(LocalDateTime now) {
        LocalDateTime currentAppointmentStartTime = LocalDateTime.parse(this.appointmentStartTime, DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN));
        return (int)(this.avgWaitingTime + currentAppointmentStartTime
                .until(now, ChronoUnit.MINUTES)) / 2;
    }

    private AppointmentStatus buildAppointmentStatus(WalkInAppointment nextWalkInAppointment, LocalDateTime now, int newAvgWaitTime) {
        AppointmentStatus appointmentStatus = new AppointmentStatus();
        appointmentStatus.setDoctorId(nextWalkInAppointment.getAppointedDoctorId());
        appointmentStatus.setAppointmentStartTime(now.format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)));
        appointmentStatus.setAvgWaitingTime(newAvgWaitTime);
        appointmentStatus.setCurrentAppointmentId(nextWalkInAppointment.getWalkInAppointmentId());
        log.debug(String.format("New Appointment Status for the doctor [%s]. Appointment Status: [%s]", appointmentStatus.getDoctorId(), appointmentStatus));
        return appointmentStatus;
    }

    private Optional<WalkInAppointment> nextWalkInAppointment(List<WalkInAppointment> walkInAppointmentList, int appointmentId) {
        // Find the next appointment
        Iterator<WalkInAppointment> itr = walkInAppointmentList.iterator();
        while (itr.hasNext()) {
            WalkInAppointment nextWalkInAppointment = itr.next();
            if (nextWalkInAppointment.getWalkInAppointmentId().equals(appointmentId)
                    && itr.hasNext()) {
                nextWalkInAppointment = itr.next();
                log.info("Next WalkInAppointment: " + nextWalkInAppointment);
                return Optional.of(nextWalkInAppointment);
            }
        }
        return Optional.empty();
    }

    public AppointmentStatus calculateNewAvgWaitTime(Integer breakTime) {
        this.avgWaitingTime = (avgWaitingTime + breakTime) / 2;
        return this;
    }

    public static AppointmentStatus newInstanceForFirstTime(Integer appointmentId, Integer doctorId, int patientsBeforeThisAppointmentId, LocalDateTime now) {
        AppointmentStatus appointmentStatus = new AppointmentStatus();
        appointmentStatus.setAvgWaitingTime(15 * patientsBeforeThisAppointmentId);
        appointmentStatus.setCurrentAppointmentId(appointmentId);
        appointmentStatus.setDoctorId(doctorId);
        appointmentStatus.setAppointmentStartTime(now.format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)));
        appointmentStatus.setPatientsInVisitedQueue(1);
        return appointmentStatus;
    }

    public int calculateAvgWaitingTime(LocalDateTime inquiryTime, int patientsBeforeThisAppointmentId) {

        int patientsInVisitedQueue = this.getPatientsInVisitedQueue();

        int minutesPassed = (int) getFormattedAppointmentStartTime()
                .until(inquiryTime, ChronoUnit.MINUTES);
        int appointmentsRemainingBeforeThisAppointment = patientsBeforeThisAppointmentId - patientsInVisitedQueue;
        int avgWaitingTimeTillCurrentAppointment = this.getAvgWaitingTime();
        int newAvgWaitingTime = (avgWaitingTimeTillCurrentAppointment + minutesPassed) / 2;
        int approxAvgWaitTime = (newAvgWaitingTime * (appointmentsRemainingBeforeThisAppointment)) + minutesPassed
                + this.getDoctorBreakDuration();

        System.out.println("Patients before this appointment: " + patientsBeforeThisAppointmentId);
        System.out.println("Minutes passed from current appointment start time to inquiry time: " + minutesPassed);
        System.out.println("Patients in the visited queue: " + patientsInVisitedQueue);
        System.out.println("Patients remaining to be checked before this appointment: " + appointmentsRemainingBeforeThisAppointment);
        System.out.println("Avg waiting time till current appointment: " + avgWaitingTimeTillCurrentAppointment);
        System.out.println("New avg waiting time: " + newAvgWaitingTime);
        System.out.println("Approx. Avg. Waiting time for this appointment: " + approxAvgWaitTime);
        System.out.println("Doctor Break Time: " + this.getDoctorBreakDuration());
        return approxAvgWaitTime;
    }

    private LocalDateTime getFormattedAppointmentStartTime() {
        return LocalDateTime.parse(
                this.getAppointmentStartTime(), DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN));
    }
}
