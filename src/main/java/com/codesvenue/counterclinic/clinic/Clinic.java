package com.codesvenue.counterclinic.clinic;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Data
@NoArgsConstructor
public class Clinic {

    private int clinicId;

    private String clinicName;
    private List<ClinicRoom> rooms;

    public Clinic(String clinicName) {
        this.clinicName = clinicName;
    }

    private Clinic(Clinic clinic) {
        this.clinicId = clinic.getClinicId();
        this.clinicName = clinic.getClinicName();
        this.rooms = clinic.getRooms();
    }

    public static Clinic newInstance(String clinicName) {
        return new Clinic(clinicName);
    }

    public static Clinic copyInstance(Clinic clinic) {
        return new Clinic(clinic);
    }

    public Clinic addNewRoom(ClinicRoom clinicRoom) {
        if (Objects.isNull(rooms))
            rooms = new LinkedList<>();
        rooms.add(clinicRoom);
        return this;
    }

    public List<ClinicRoom> getClinicRooms() {
        return Collections.unmodifiableList(rooms);
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
