package com.codesvenue.counterclinic.walkinappointment.dao;

import com.codesvenue.counterclinic.configuration.DateTimeConstants;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.dao.DatabaseException;
import com.codesvenue.counterclinic.walkinappointment.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
@Log4j
public class AppointmentRepositoryMySql implements AppointmentRepository{
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AppointmentRepositoryMySql(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<WalkInAppointment> fetchDoctorAppointmentsForToday(Integer doctorId) {
        final String sql = "SELECT t1.walkin_appointment_id, t1.patient_first_name, t1.patient_last_name, t1.appointed_doctor_id, t1.appointment_number, t1.created_at \n" +
                "FROM walkin_appointments t1 \n" +
                "WHERE t1.appointed_doctor_id=:doctorId AND SUBSTRING(created_at, 0, 11) = CURRENT_DATE";
        SqlParameterSource params = new MapSqlParameterSource().addValue("doctorId", doctorId);
        return jdbcTemplate.query(sql, params, WalkInAppointment.WalkInAppointmentRowMapper.newInstance());
    }

    @Override
    public List<AppointmentStatus> fetchAppointmentStatusList(Integer doctorId) {
        final String sql = "SELECT t1.walkin_appointment_status_id, t1.current_appointment_id, t1.doctor_id, t1.avg_wait_time, t1.appointment_start_datetime, t1.doctor_break_duration, t1.patients_in_visited_queue, \n" +
                "(SELECT COUNT(1) FROM walkin_appointments WHERE t1.doctor_id=:doctorId AND SUBSTRING(created_at, 0, 11) = CURRENT_DATE) as total_appointments\n" +
                "FROM walkin_appointment_status t1\n" +
                "WHERE t1.doctor_id=:doctorId\n" +
                "ORDER BY t1.walkin_appointment_status_id DESC\n";
        SqlParameterSource params = new MapSqlParameterSource().addValue("doctorId", doctorId);
        return jdbcTemplate.query(sql, params, AppointmentStatus.AppointmentStatusRowMapper.newInstance());
    }

    @Override
    public List<AppointmentStatus> fetchAppointmentStatusListForToday(Integer doctorId) {
        final String sql = "SELECT t1.walkin_appointment_status_id, t1.current_appointment_id, t1.doctor_id, t1.avg_wait_time, t1.appointment_start_datetime, t1.doctor_break_duration, t1.patients_in_visited_queue, \n" +
                "(SELECT COUNT(1) FROM walkin_appointments WHERE t1.doctor_id=:doctorId AND SUBSTRING(created_at, 0, 11) = CURRENT_DATE) as total_appointments\n" +
                "FROM walkin_appointment_status t1\n" +
                "WHERE t1.doctor_id=:doctorId\n" +
                "AND SUBSTRING(appointment_start_datetime, 0, 11) = CURRENT_DATE \n" +
                "ORDER BY t1.walkin_appointment_status_id DESC\n";
        SqlParameterSource params = new MapSqlParameterSource().addValue("doctorId", doctorId);
        return jdbcTemplate.query(sql, params, AppointmentStatus.AppointmentStatusRowMapper.newInstance());
    }

    @Override
    public AppointmentStatus saveAppointmentStatus(AppointmentStatus appointmentStatus) {
        final String sql = "INSERT INTO walkin_appointment_status (\n" +
                "\tcurrent_appointment_id, \n" +
                "    doctor_id, \n" +
                "    avg_wait_time, \n" +
                "    appointment_start_datetime, \n" +
                "    doctor_break_duration,\n" +
                "    patients_in_visited_queue\n" +
                ") VALUES (\n" +
                "    :currentAppointmentId, \n" +
                "    :doctorId, \n" +
                "    :avgWaitTime, \n" +
                "    :appointmentStartDateTime, \n" +
                "    :doctorBreakDuration, \n" +
                "    :patientsInVisitedQueue \n" +
//                "    (patients_in_visited_queue + 1)\n" +
                ")";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("currentAppointmentId", appointmentStatus.getCurrentAppointmentId())
                .addValue("doctorId", appointmentStatus.getDoctorId())
                .addValue("avgWaitTime", appointmentStatus.getAvgWaitingTime())
                .addValue("appointmentStartDateTime", appointmentStatus.getAppointmentStartTimeFormatted())
                .addValue("doctorBreakDuration", appointmentStatus.getDoctorBreakDuration())
                .addValue("patientsInVisitedQueue", appointmentStatus.getPatientsInVisitedQueue());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return AppointmentStatus.copyInstance(appointmentStatus)
                .appointmentStatusId((int)keyHolder.getKeys().get("WALKIN_APPOINTMENT_STATUS_ID"))
                .totalAppointments(appointmentStatus.getTotalAppointments());
    }

    @Override
    public WalkInAppointments fetchAllWalkInAppointments() {
        String sql = "SELECT t1.walkin_appointment_id, t1.patient_first_name, t1.patient_last_name, t1.appointed_doctor_id, t1.appointment_number, t1.created_at FROM `walkin_appointments` t1 ORDER BY created_at DESC";
        List<WalkInAppointment> walkInAppointmentList= jdbcTemplate.query(sql, WalkInAppointment.WalkInAppointmentRowMapper.newInstance());
        WalkInAppointments walkInAppointments = new WalkInAppointments();
        walkInAppointments.setWalkInAppointmentList(walkInAppointmentList);
        return walkInAppointments;
    }

    @Override
    public int deleteWalkInAppointment(int appointmentId) {
        String sql = "DELETE FROM walkin_appointments WHERE walkin_appointment_id=:appointmentId";
        SqlParameterSource params = new MapSqlParameterSource().addValue("appointmentId", appointmentId);
        return jdbcTemplate.update(sql, params);
    }

    @Override
    public QRCode deleteQrCodeAttachment(int appointmentId) {
        QRCode qrCode = fetchQrCodeAttachment(appointmentId);
        final String sql = "DELETE FROM qrcode_attachments WHERE appointment_id = :appointmentId";
        SqlParameterSource params = new MapSqlParameterSource().addValue("appointmentId", appointmentId);
        jdbcTemplate.update(sql, params);
        return qrCode;
    }

    @Override
    public QRCode fetchQrCodeAttachment(int appointmentId) {
        final String sql = "SELECT qrcode_id, appointment_id, height, width, image_name, image_file_path, image_url_path, qrcode_data, created_at FROM qrcode_attachments WHERE appointment_id = :appointmentId";
        SqlParameterSource params = new MapSqlParameterSource().addValue("appointmentId", appointmentId);
        return jdbcTemplate.queryForObject(sql, params, QRCode.QRCodeRowMapper.newInstance());
    }

    @Override
    public List<WalkInAppointmentWithAttachment> fetchAllWalkInAppointmentsWithAttachments() {
        final String sql = "SELECT t1.walkin_appointment_id, t1.patient_first_name, t1.patient_last_name, t1.appointed_doctor_id, t1.appointment_number, t1.created_at, t2.qrcode_id, t2.height, t2.width, t2.image_name, t2.image_file_path, t2.image_url_path, t2.qrcode_data\n" +
                "FROM walkin_appointments t1 \n" +
                "LEFT JOIN qrcode_attachments t2\n" +
                "ON t2.appointment_id = t1.walkin_appointment_id\n" +
                "ORDER BY t1.created_at DESC\n";
        return jdbcTemplate.query(sql, WalkInAppointmentWithAttachment.WalkInAppointmentWithAttachmentRowMapper.newInstance());
    }

    @Override
    public WalkInAppointmentWrapper findWalkInAppointmentById(int appointmentId) {

        final String sql = "SELECT t1.walkin_appointment_id, t1.walkin_appointment_id as appointment_id, t1.walkin_appointment_id, t1.patient_first_name, t1.patient_last_name, t1.appointed_doctor_id, t1.appointment_number, t1.created_at, t2.user_id, t2.first_name, t2.last_name, t2.email, t2.mobile, t2.username, t2.preferred_language, t3.qrcode_id, t3.height, t3.width, t3.image_name, t3.image_file_path, t3.image_url_path, t3.qrcode_data,\n" +
                "(SELECT GROUP_CONCAT(DISTINCT t2.role_name) as user_role FROM user_roles t2 WHERE t2.user_id = t1.appointed_doctor_id AND user_id = t1.appointed_doctor_id ) as user_roles" + "," +
                "(SELECT meta_value FROM users_meta WHERE meta_key = 'assigned_clinic_room' AND user_id = t1.appointed_doctor_id) as assigned_clinic_room" + "\n " +
                "FROM walkin_appointments t1\n" +
                "LEFT JOIN users t2\n" +
                "ON t1.appointed_doctor_id = t2.user_id\n" +
                "LEFT JOIN qrcode_attachments t3\n" +
                "ON t1.walkin_appointment_id = t3.appointment_id\n" +
                "WHERE t1.walkin_appointment_id = :appointmentId";
        SqlParameterSource params = new MapSqlParameterSource().addValue("appointmentId", appointmentId);
        return jdbcTemplate.queryForObject(sql, params, WalkInAppointmentWrapper.WalkInAppointmentWrapperRowMapper.newInstance());
    }

    @Override
    public Optional<AppointmentStatus> findLatestAppointmentStatusByDoctorId(Integer userId) {
        try {
            final String sql = "SELECT t1.walkin_appointment_status_id, t1.current_appointment_id, t1.doctor_id, t1.avg_wait_time, t1.appointment_start_datetime, t1.doctor_break_duration, t1.patients_in_visited_queue, \n" +
                    "(SELECT COUNT(1) FROM walkin_appointments WHERE t1.doctor_id=:doctorId AND SUBSTRING(created_at, 0, 11) = CURRENT_DATE) as total_appointments,\n" +
                    "(SELECT meta_value FROM users_meta WHERE meta_key = 'assigned_clinic_room' AND user_id = t1.doctor_id) as assigned_clinic_room \n" +
                    "FROM walkin_appointment_status t1\n" +
                    "WHERE t1.doctor_id=:doctorId\n" +
                    "ORDER BY t1.walkin_appointment_status_id DESC\n" +
                    "LIMIT 1";
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("doctorId", userId);
            return Optional.of(jdbcTemplate.queryForObject(sql, params, AppointmentStatus.AppointmentStatusRowMapper.newInstance()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AppointmentStatus> findLatestAppointmentStatusByDoctorIdForToday(Integer doctorId) {
        try {
            final String sql = "SELECT t1.walkin_appointment_status_id, t1.current_appointment_id, t1.doctor_id, t1.avg_wait_time, t1.appointment_start_datetime, t1.doctor_break_duration, t1.patients_in_visited_queue, \n" +
                    "(SELECT COUNT(1) FROM walkin_appointments WHERE t1.doctor_id=:doctorId AND SUBSTRING(created_at, 0, 11) = CURRENT_DATE) as total_appointments,\n" +
                    "(SELECT meta_value FROM users_meta WHERE meta_key = 'assigned_clinic_room' AND user_id = t1.doctor_id LIMIT  1) as assigned_clinic_room \n" +
                    "FROM walkin_appointment_status t1\n" +
                    "WHERE t1.doctor_id=:doctorId\n " +
                    "AND SUBSTRING(appointment_start_datetime, 0, 11) = CURRENT_DATE \n" +
                    "ORDER BY t1.walkin_appointment_status_id DESC\n" +
                    "LIMIT 1";
            MapSqlParameterSource params = new MapSqlParameterSource().addValue("doctorId", doctorId);
            return Optional.of(jdbcTemplate.queryForObject(sql, params, AppointmentStatus.AppointmentStatusRowMapper.newInstance()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public WalkInAppointment createNewWalkInAppointment(WalkInAppointment walkInAppointment) {
        final String sql = "INSERT INTO walkin_appointments (patient_first_name, patient_last_name, appointed_doctor_id, appointment_number, created_at) " +
                "VALUES (:patientFirstName, :patientLastName, :appointedDoctorId, :appointmentNumber, :createdAt)";
        final LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("patientFirstName", walkInAppointment.getPatientFirstName())
                .addValue("patientLastName", walkInAppointment.getPatientLastName())
                .addValue("appointedDoctorId", walkInAppointment.getAppointedDoctorId())
                .addValue("appointmentNumber", walkInAppointment.getAppointmentNumber())
                .addValue("createdAt", now
                        .format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return WalkInAppointment.copyInstance(walkInAppointment)
                .walkInAppointmentId(keyHolder.getKey().intValue())
                .createdAt(now.format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)));
    }

    @Override
    public QRCode createNewQRCode(QRCode qrCode) {
        final String sql = "INSERT INTO qrcode_attachments (appointment_id, height, width, image_name, image_file_path, image_url_path, qrcode_data, created_at) " +
                "VALUES (:appointmentId, :height, :width, :imageName, :imageFilePath, :imageUrlPath, :qrCodeData, :createdAt)";
        SqlParameterSource params = null;
        try {
            params = new MapSqlParameterSource()
                    .addValue("appointmentId", qrCode.getAppointmentId())
                    .addValue("height", qrCode.getQrCodeHeight())
                    .addValue("width", qrCode.getQrCodeWidth())
                    .addValue("imageName", qrCode.getQrCodeName())
                    .addValue("imageFilePath", qrCode.getQrCodeFilePath())
                    .addValue("imageUrlPath", qrCode.getQrCodeUrlPath())
                    .addValue("qrCodeData", qrCode.getQrCodeDataInJson())
                    .addValue("createdAt", LocalDateTime.now(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)));
        } catch (JsonProcessingException e) {
            log.error("Cannot convert qrcode data to json. Error: " + e.getMessage(), e);
            throw new DatabaseException("Cannot convert qrcode data to json. Error: " + e.getMessage());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);

        return QRCode.copyInstance(qrCode).qrCodeId(keyHolder.getKey().intValue());
    }
}
