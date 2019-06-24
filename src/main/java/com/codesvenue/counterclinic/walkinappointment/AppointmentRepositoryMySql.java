package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.UserRepositoryMySql;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppointmentRepositoryMySql implements AppointmentRepository{
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AppointmentRepositoryMySql(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<WalkInAppointment> fetchDoctorAppointmentsForToday(Integer doctorId) {
        return null;
    }

    @Override
    public List<AppointmentStatus> fetchAppointmentStatusList(Integer doctorId) {
        return null;
    }

    @Override
    public AppointmentStatus saveAppointmentStatus(AppointmentStatus appointmentStatus) {
        return null;
    }

    @Override
    public WalkInAppointments fetchAllWalkInAppointments() {
        String sql = "SELECT t1.walkin_appointment_id, t1.patient_first_name, t1.patient_last_name, t1.appointed_doctor_id, t1.created_at FROM `walkin_appointments` t1 ORDER BY created_at DESC";
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
        final String sql = "SELECT qrcode_id, appointment_id, height, width, image_name, image_file_path, image_url_path, qrcode_data, created_at FROM `qrcode_attachments` WHERE appointment_id = :appointmentId";
        SqlParameterSource params = new MapSqlParameterSource().addValue("appointmentId", appointmentId);
        return jdbcTemplate.queryForObject(sql, params, QRCode.QRCodeRowMapper.newInstance());
    }

    @Override
    public List<WalkInAppointmentWithAttachment> fetchAllWalkInAppointmentsWithAttachments() {
        final String sql = "SELECT t1.walkin_appointment_id, t1.patient_first_name, t1.patient_last_name, t1.appointed_doctor_id, t1.created_at, t2.qrcode_id, t2.height, t2.width, t2.image_name, t2.image_file_path, t2.image_url_path, t2.qrcode_data\n" +
                "FROM `walkin_appointments` t1 \n" +
                "LEFT JOIN qrcode_attachments t2\n" +
                "ON t2.appointment_id = t1.walkin_appointment_id\n" +
                "ORDER BY t1.created_at DESC\n";
        return jdbcTemplate.query(sql, WalkInAppointmentWithAttachment.WalkInAppointmentWithAttachmentRowMapper.newInstance());
    }

    @Override
    public WalkInAppointmentWrapper findWalkInAppointmentById(int appointmentId) {
        final String sql = "SELECT t1.walkin_appointment_id, t1.walkin_appointment_id, t1.patient_first_name, t1.patient_last_name, t1.appointed_doctor_id, t1.created_at, t2.user_id, t2.first_name, t2.last_name, t2.email, t2.mobile, t2.username, t2.preferred_language, t3.qrcode_id, t3.height, t3.width, t3.image_name, t3.image_file_path, t3.image_url_path, t3.qrcode_data,\n" +
                " (SELECT GROUP_CONCAT(DISTINCT t2.role_name) as user_role FROM user_roles t2 WHERE t2.user_id = t1.appointed_doctor_id ) as user_roles," +
                " (SELECT meta_value FROM users_meta WHERE meta_key = 'assigned_clinic_room') as assigned_clinic_room\n " +
                "FROM walkin_appointments t1\n" +
                "LEFT JOIN users t2\n" +
                "ON t1.appointed_doctor_id = t2.user_id\n" +
                "LEFT JOIN qrcode_attachments t3\n" +
                "ON t1.walkin_appointment_id = t3.appointment_id\n" +
                "WHERE t1.walkin_appointment_id = :appointmentId";
        SqlParameterSource params = new MapSqlParameterSource().addValue("appointmentId", appointmentId);
        return jdbcTemplate.queryForObject(sql, params, WalkInAppointmentWrapper.WalkInAppointmentWrapperRowMapper.newInstance());
    }
}
