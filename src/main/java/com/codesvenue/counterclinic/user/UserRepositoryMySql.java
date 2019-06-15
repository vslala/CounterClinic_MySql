package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicRoom;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        return null;
    }

    @Override
    public User findDoctorById(Integer doctorId) {
        return null;
    }

    @Override
    public QRCode createNewQRCode(QRCode qrCode) {
        return null;
    }

    @Override
    public WalkInAppointment findAppointmentById(Integer nextAppointmentId) {
        return null;
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
}
