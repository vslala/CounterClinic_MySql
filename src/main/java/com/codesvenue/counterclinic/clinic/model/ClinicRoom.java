package com.codesvenue.counterclinic.clinic.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@Data
public class ClinicRoom {

    private Integer clinicId;
    private Integer clinicRoomId;
    private String name;

    public ClinicRoom(Integer clinicId, Integer clinicRoomId, String name) {
        this.clinicId = clinicId;
        this.clinicRoomId = clinicRoomId;
        this.name = name;
    }

    public ClinicRoom(String name) {
        this.name = name;
    }

    private ClinicRoom(ClinicRoom room) {
        this.name = room.getName();
    }

    public static ClinicRoom newInstance(String name) {
        return new ClinicRoom(name);
    }

    public static ClinicRoom newInstanceWithClinicId(Integer clinicId, String clinicRoomName) {
        ClinicRoom clinicRoom = new ClinicRoom();
        clinicRoom.setClinicId(clinicId);
        clinicRoom.setName(clinicRoomName);
        return clinicRoom;
    }

    public static ClinicRoom copyInstance(ClinicRoom room) {
        return new ClinicRoom(room);
    }

    public ClinicRoom clinicRoomId(Integer clinicRoomId) {
        this.clinicRoomId = clinicRoomId;
        return this;
    }

    public static class ClinicRoomRowMapper implements RowMapper<ClinicRoom> {

        public static ClinicRoomRowMapper newInstance() {
            return new ClinicRoomRowMapper();
        }

        @Override
        public ClinicRoom mapRow(ResultSet resultSet, int i) throws SQLException {
            return new ClinicRoom(
                    resultSet.getInt("clinic_id"),
                    resultSet.getInt("clinic_room_id"),
                    resultSet.getString("room_name"));
        }
    }
}
