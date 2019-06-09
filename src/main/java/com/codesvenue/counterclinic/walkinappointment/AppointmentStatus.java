package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.configuration.DateTimeConstants;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

@Data
public class AppointmentStatus {
    private Integer appointmentStatusId;
    private Integer currentAppointmentId;
    private Integer doctorId;
    private Integer avgWaitingTime = 0;
    private String appointmentStartTime;
    private Integer doctorBreakTime = 0;

    public AppointmentStatus generateAppointmentStatus(List<WalkInAppointment> walkInAppointmentList) {
        // Fetch the last appointment status from the list
        int appointmentId = this.getCurrentAppointmentId();

        // Find the next appointment
        WalkInAppointment nextWalkInAppointment = null;
        Iterator<WalkInAppointment> itr = walkInAppointmentList.iterator();
        while (itr.hasNext()) {
            nextWalkInAppointment = itr.next();
            if (nextWalkInAppointment.getWalkInAppointmentId().equals(appointmentId)
                    && itr.hasNext()) {
                nextWalkInAppointment = itr.next();
            }
        }

        // calculate new average
        LocalDateTime currentAppointmentStartTime = LocalDateTime.parse(this.appointmentStartTime, DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN));
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        int newAvgWaitTime = (int)(this.avgWaitingTime + currentAppointmentStartTime
                .until(now, ChronoUnit.MINUTES)) / 2;

        AppointmentStatus appointmentStatus = new AppointmentStatus();
        appointmentStatus.setDoctorId(nextWalkInAppointment.getAppointedDoctor().getUserId());
        appointmentStatus.setAppointmentStartTime(now.format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)));
        appointmentStatus.setAvgWaitingTime(newAvgWaitTime);
        appointmentStatus.setCurrentAppointmentId(nextWalkInAppointment.getWalkInAppointmentId());
        return appointmentStatus;
    }
}
