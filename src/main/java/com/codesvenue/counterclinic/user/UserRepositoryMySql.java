package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicRoom;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Log4j
public class UserRepositoryMySql implements UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepositoryMySql(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Clinic createNewClinic(Clinic clinic) {
        final String SQL = "INSERT INTO clinics (clinic_name) values (:clinicName)";
        SqlParameterSource params = new MapSqlParameterSource().addValue("clinicName", clinic.getClinicName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL, params, keyHolder);

        Clinic newClinic = Clinic.copyInstance(clinic);
        newClinic.setClinicId(keyHolder.getKey().intValue());

        return newClinic;
    }

    @Override
    public WalkInAppointment createNewWalkInAppointment(WalkInAppointment walkInAppointment) {
        final String sql = "INSERT INTO walkin_appointments (patient_first_name, patient_last_name, appointed_doctor_id) " +
                "VALUES (:patientFirstName, :patientLastName, :appointedDoctorId)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("patientFirstName", walkInAppointment.getPatientFirstName())
                .addValue("patientLastName", walkInAppointment.getPatientLastName())
                .addValue("appointedDoctorId", walkInAppointment.getAppointedDoctorId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return WalkInAppointment.copyInstance(walkInAppointment)
                .walkInAppointmentId(keyHolder.getKey().intValue());
    }

    @Override
    public User findDoctorById(Integer doctorId) {
        final String sql = "SELECT t1.user_id, t1.first_name, t1.last_name, t1.email, t1.mobile, t1.username, t1.preferred_language, t1.created_at,\n" +
                "\t(SELECT t2.meta_value FROM users_meta t2 WHERE t2.meta_key = :userRole AND t2.user_id = :userId) as user_role,\n" +
                "\t(SELECT t2.meta_value FROM users_meta t2 WHERE t2.meta_key = :assignedClinicRoom AND t2.user_id = :userId) as assigned_clinic_room\n" +
                "FROM users t1\n" +
                "WHERE t1.user_id = :userId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userRole", UserConstants.USER_ROLE)
                .addValue("userId", doctorId)
                .addValue("assignedClinicRoom", UserConstants.ASSIGNED_CLINIC_ROOM);
        return jdbcTemplate.queryForObject(sql, params, User.UserRowMapper.newInstance());
    }

    @Override
    public QRCode createNewQRCode(QRCode qrCode) {
        final String sql = "INSERT INTO qrcode_attachments (appointment_id, height, width, image_name, image_file_path, image_url_path, qrcode_data) " +
                "VALUES (:appointmentId, :height, :width, :imageName, :imageFilePath, :imageUrlPath, :qrCodeData)";
        SqlParameterSource params = null;
        try {
            params = new MapSqlParameterSource()
                    .addValue("appointmentId", qrCode.getAppointmentId())
                    .addValue("height", qrCode.getQrCodeHeight())
                    .addValue("width", qrCode.getQrCodeWidth())
                    .addValue("imageName", qrCode.getQrCodeName())
                    .addValue("imageFilePath", qrCode.getQrCodeFilePath())
                    .addValue("imageUrlPath", qrCode.getQrCodeUrlPath())
                    .addValue("qrCodeData", qrCode.getQrCodeDataInJson());
        } catch (JsonProcessingException e) {
            log.error("Cannot convert qrcode data to json. Error: " + e.getMessage(), e);
            throw new DatabaseException("Cannot convert qrcode data to json. Error: " + e.getMessage());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);

        return QRCode.copyInstance(qrCode).qrCodeId(keyHolder.getKey().intValue());
    }

    @Override
    public WalkInAppointment findAppointmentById(Integer nextAppointmentId) {
        final String sql = "SELECT t1.walkin_appointment_id, t1.patient_first_name, t1.patient_last_name, " +
                "t1.appointed_doctor_id, t1.created_at FROM `walkin_appointments` t1 " +
                "WHERE t1.walkin_appointment_id = :walkInAppointmentId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("walkInAppointmentId", nextAppointmentId);
        return jdbcTemplate.queryForObject(sql, params, WalkInAppointmentRowMapper.newInstance());
    }

    @Override
    public Clinic createNewClinicRoom(Clinic newClinic) {
        final String SQL = "INSERT INTO clinic_rooms (clinic_id, room_name) values (:clinicId, :roomName)";
        List<ClinicRoom> clinicRooms = new ArrayList<>();
        newClinic.getRooms().forEach(room -> {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(SQL, new MapSqlParameterSource()
                    .addValue("clinicId", newClinic.getClinicId())
                    .addValue("roomName", room.getName()),
                    keyHolder);
            clinicRooms.add(ClinicRoom.copyInstance(room));
                });
        newClinic.setRooms(clinicRooms);
        return newClinic;
    }

    @Override
    public User createNewUser(User user) {
        final String sql = "INSERT INTO users (first_name, last_name, email, mobile, username, preferred_language) " +
                "values (:firstName, :lastName, :email, :mobile, :username, :preferredLanguage)";
        System.out.println("Preferred Language: "  + user.getPreferredLanguage());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("mobile", user.getMobile())
                .addValue("username", user.getUsername())
                .addValue("preferredLanguage", user.getPreferredLanguage().toString());
        jdbcTemplate.update(sql, params, keyHolder);
        return User.copyInstance(user).userId(keyHolder.getKey().intValue());
    }

    @Override
    public UserLogin createNewUserLogin(UserLogin userLogin) {
        final String sql = "INSERT INTO users_login (user_id, username, password, logged_in_at) " +
                "values (:userId, :username, :password,  :loggedInAt)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userLogin.getUserId())
                .addValue("username", userLogin.getUsername())
                .addValue("password", userLogin.getPassword())
                .addValue("loggedInAt", userLogin.getLoggedInAt());
        jdbcTemplate.update(sql, params, keyHolder);
        return UserLogin.copyInstance(userLogin).id(keyHolder.getKey().intValue());
    }

    @Override
    public ClinicRoom findClinicRoomById(Integer clinicRoomId) {
        final String sql = "SELECT clinic_room_id, clinic_id, room_name FROM clinic_rooms WHERE clinic_room_id = :clinicRoomId";
        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource().addValue("clinicRoomId", clinicRoomId),
                ClinicRoomRowMapper.newInstance());
    }

    @Override
    public UserMeta updateUserMeta(Integer userId, String metaKey, String metaValue) {
        final String sql = "INSERT INTO users_meta (user_id, meta_key, meta_value) " +
                "VALUES (:userId, :metaKey, :metaValue) " +
                "ON DUPLICATE KEY UPDATE " +
                "user_id = :userId, meta_key=:metaKey, meta_value=:metaValue";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("metaKey", metaKey)
                .addValue("metaValue", metaValue);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(sql, params, keyHolder);
        return UserMeta.newInstance()
                .metaId( Objects.isNull(keyHolder.getKeys()) ? 0 : (int)keyHolder.getKeys().getOrDefault("meta_id", 0))
                .userId(userId)
                .metaKey(metaKey)
                .metaValue(metaValue);
    }

    public static class WalkInAppointmentRowMapper implements RowMapper<WalkInAppointment> {
        public static WalkInAppointmentRowMapper newInstance() {
            return new WalkInAppointmentRowMapper();
        }

        @Override
        public WalkInAppointment mapRow(ResultSet resultSet, int i) throws SQLException {
            WalkInAppointment walkInAppointment = new WalkInAppointment();
            walkInAppointment.setWalkInAppointmentId(resultSet.getInt("walkin_appointment_id"));
            walkInAppointment.setPatientFirstName(resultSet.getString("patient_first_name"));
            walkInAppointment.setPatientLastName(resultSet.getString("patient_last_name"));
            walkInAppointment.setAppointedDoctorId(resultSet.getInt("appointed_doctor_id"));
            return walkInAppointment;
        }
    }

    public static class ClinicRoomRowMapper implements RowMapper<ClinicRoom> {

        public static ClinicRoomRowMapper newInstance() {
            return new ClinicRoomRowMapper();
        }


        @Override
        public ClinicRoom mapRow(ResultSet resultSet, int i) throws SQLException {
            ClinicRoom clinicRoom = new ClinicRoom();
            clinicRoom.setName(resultSet.getString("room_name"));
            clinicRoom.setClinicRoomId(resultSet.getInt("clinic_room_id"));
            return clinicRoom;
        }
    }
}
