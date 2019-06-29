package com.codesvenue.counterclinic.walkinappointment.model;

import com.codesvenue.counterclinic.configuration.DateTimeConstants;
import com.codesvenue.counterclinic.walkinappointment.dao.NoMoreAppointmentsException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
@Data
@Log4j
public class AppointmentStatus {
    private Integer appointmentStatusId;
    private Integer currentAppointmentId;
    private Integer doctorId;
    private Integer avgWaitingTime = 0;
    private LocalDateTime appointmentStartTime;
    private Integer doctorBreakDuration = 0;
    private Integer patientsInVisitedQueue = 0;
    private Integer totalAppointments;

    public static AppointmentStatus newInstanceWithApproxAvgWaitTime(final Integer appointmentId,
                                                                     final int approxAvgWaitTime,
                                                                     final AppointmentStatus lastAppointmentStatus) {
        AppointmentStatus newAppointmentStatus = lastAppointmentStatus;
        newAppointmentStatus.setCurrentAppointmentId(appointmentId);
        newAppointmentStatus.setAvgWaitingTime(approxAvgWaitTime);
        return newAppointmentStatus;
    }

    private AppointmentStatus(AppointmentStatus other) {
        this.appointmentStatusId = other.appointmentStatusId;
        this.currentAppointmentId = other.currentAppointmentId;
        this.doctorId = other.doctorId;
        this.avgWaitingTime = other.avgWaitingTime;
        this.appointmentStartTime = other.appointmentStartTime;
        this.doctorBreakDuration = other.doctorBreakDuration;
        this.patientsInVisitedQueue = other.patientsInVisitedQueue;
    }

    public static AppointmentStatus copyInstance(AppointmentStatus appointmentStatus) {
        return new AppointmentStatus(appointmentStatus);
    }

    public AppointmentStatus generateAppointmentStatus(WalkInAppointment nextWalkInAppointment, LocalDateTime creationTime) {
        // Fetch the last appointment status from the list
        int appointmentId = this.getCurrentAppointmentId();

        // calculate new average
        int newAvgWaitTime = calcNewAvgWaitTime(creationTime);

        return buildAppointmentStatus(nextWalkInAppointment, creationTime, newAvgWaitTime);
    }

    private int calcNewAvgWaitTime(LocalDateTime now) {
        LocalDateTime currentAppointmentStartTime = this.appointmentStartTime;
        return (int)(this.avgWaitingTime + currentAppointmentStartTime
                .until(now, ChronoUnit.MINUTES)) / 2;
    }

    private AppointmentStatus buildAppointmentStatus(WalkInAppointment nextWalkInAppointment, LocalDateTime now, int newAvgWaitTime) {
        AppointmentStatus appointmentStatus = new AppointmentStatus();
        appointmentStatus.setDoctorId(nextWalkInAppointment.getAppointedDoctorId());
        appointmentStatus.setAppointmentStartTime(now);
        appointmentStatus.setAvgWaitingTime(newAvgWaitTime);
        appointmentStatus.setCurrentAppointmentId(nextWalkInAppointment.getWalkInAppointmentId());
        appointmentStatus.setPatientsInVisitedQueue(this.patientsInVisitedQueue + 1);
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
        log.debug("Avg Waiting Time: " + avgWaitingTime + ", Break Time: " + breakTime);
        this.avgWaitingTime = avgWaitingTime + breakTime;
        return this;
    }

    public static AppointmentStatus newInstanceForFirstTime(Integer appointmentId, Integer doctorId, int patientsBeforeThisAppointmentId, LocalDateTime now) {
        AppointmentStatus appointmentStatus = new AppointmentStatus();
        appointmentStatus.setAvgWaitingTime(Math.abs(15 * patientsBeforeThisAppointmentId));
        appointmentStatus.setCurrentAppointmentId(appointmentId);
        appointmentStatus.setDoctorId(doctorId);
        appointmentStatus.setAppointmentStartTime(now);
        appointmentStatus.setPatientsInVisitedQueue(patientsBeforeThisAppointmentId);
        return appointmentStatus;
    }

    public int calculateAvgWaitingTime(LocalDateTime inquiryTime, int patientsBeforeThisAppointmentId) {

        int patientsInVisitedQueue = this.getPatientsInVisitedQueue();

        int minutesPassed = (int) getAppointmentStartTime()
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

    public LocalDateTime getAppointmentStartTime() {
        return this.appointmentStartTime;
    }

    public String getAppointmentStartTimeFormatted() {
        if (Objects.isNull(appointmentStartTime))
            return "";
        return getAppointmentStartTime().format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN));
    }

    public AppointmentStatus appointmentStatusId(int appointmentStatusId) {
        this.appointmentStatusId = appointmentStatusId;
        return this;
    }
    public AppointmentStatus totalAppointments(Integer totalAppointments) {
        this.totalAppointments = totalAppointments;
        return this;
    }

    public AppointmentStatus doctorBreakDuration(int breakDuration) {
        this.doctorBreakDuration = breakDuration;
        return this;
    }

    public static class AppointmentStatusRowMapper implements RowMapper<AppointmentStatus> {

        public static AppointmentStatusRowMapper newInstance() {
            return new AppointmentStatusRowMapper();
        }

        @Override
        public AppointmentStatus mapRow(ResultSet resultSet, int i) throws SQLException {
            AppointmentStatus appointmentStatus = new AppointmentStatus();
            appointmentStatus.setAppointmentStatusId(resultSet.getInt("walkin_appointment_status_id"));
            appointmentStatus.setCurrentAppointmentId(resultSet.getInt("current_appointment_id"));
            appointmentStatus.setDoctorId(resultSet.getInt("doctor_id"));
            appointmentStatus.setAvgWaitingTime(resultSet.getInt("avg_wait_time"));
            appointmentStatus.setAppointmentStartTime(LocalDateTime.parse(
                    resultSet.getString("appointment_start_datetime"), DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)
            ));
            appointmentStatus.setDoctorBreakDuration(resultSet.getInt("doctor_break_duration"));
            appointmentStatus.setPatientsInVisitedQueue(resultSet.getInt("patients_in_visited_queue"));
            return appointmentStatus;
        }
    }
}
