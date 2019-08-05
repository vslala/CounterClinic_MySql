package com.codesvenue.counterclinic.walkinappointment.dao;

import com.codesvenue.counterclinic.configuration.DateTimeConstants;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.dao.DatabaseException;
import com.codesvenue.counterclinic.walkinappointment.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository {
    List<WalkInAppointment> fetchDoctorAppointmentsForToday(Integer doctorId);

    List<AppointmentStatus> fetchAppointmentStatusList(Integer doctorId);

    List<AppointmentStatus> fetchAppointmentStatusListForToday(Integer doctorId);

    AppointmentStatus saveAppointmentStatus(AppointmentStatus appointmentStatus);

    WalkInAppointments fetchAllWalkInAppointments();

    int deleteWalkInAppointment(int appointmentId);

    QRCode deleteQrCodeAttachment(int appointmentId);

    QRCode fetchQrCodeAttachment(int appointmentId);

    List<WalkInAppointmentWithAttachment> fetchAllWalkInAppointmentsWithAttachments();

    WalkInAppointmentWrapper findWalkInAppointmentById(int appointmentId);

    Optional<AppointmentStatus> findLatestAppointmentStatusByDoctorId(Integer userId);

    Optional<AppointmentStatus> findLatestAppointmentStatusByDoctorIdForToday(Integer doctorId);

    WalkInAppointment createNewWalkInAppointment(WalkInAppointment walkInAppointment);

    QRCode createNewQRCode(QRCode qrCode);
}
