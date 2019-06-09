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
        int patientsBeforeThisAppointmentId = WalkInAppointment.findPatientsBeforeGivenAppointmentId(appointmentId, walkInAppointmentList);

        if (appointmentStatusList.isEmpty()) {
            // doctor has not started taking patients
            return AppointmentStatus.newInstanceForFirstTime(appointmentId, doctorId, patientsBeforeThisAppointmentId);
        }

        int approxAvgWaitTime = AppointmentStatus.calculateAvgWaitingTime(inquiryTime, appointmentStatusList, patientsBeforeThisAppointmentId);

        return AppointmentStatus.newInstance(appointmentId, doctorId, inquiryTime, approxAvgWaitTime);
    }

}
