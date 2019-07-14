package com.codesvenue.counterclinic.clinic.dao;

import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.user.model.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClinicRepositoryMySql implements ClinicRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ClinicRepositoryMySql(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<ClinicRoom> fetchClinics() {
        final String sql = "SELECT clinic_room_id, clinic_id, room_name \n" +
                "FROM clinic_rooms \n";
        return jdbcTemplate.query(sql, ClinicRoom.ClinicRoomRowMapper.newInstance());
    }

    @Override
    public Integer createNewClinicRoom(Integer clinicId, String name) {
        final String sql = "INSERT INTO clinic_rooms (clinic_id, room_name) VALUES (:clinicId, :roomName)";
        SqlParameterSource params = new MapSqlParameterSource().addValue("clinicId", clinicId)
                .addValue("roomName", name);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public User assignClinicRoom(User doctor) {
        final String sql = "INSERT INTO users_meta (user_id, meta_key, meta_value) \n" +
                "VALUES (:userId, :metaKey, :metaValue) \n" +
                "ON DUPLICATE KEY UPDATE \n" +
                "user_id = :userId, meta_key=:metaKey, meta_value=:metaValue\n";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", doctor.getUserId())
                .addValue("metaKey", User.ASSIGNED_CLINIC_ROOM_KEY)
                .addValue("metaValue", doctor.getClinicRoomId());
        jdbcTemplate.update(sql, params);
        return doctor;
    }
}
