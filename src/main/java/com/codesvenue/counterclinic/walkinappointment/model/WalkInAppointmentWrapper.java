package com.codesvenue.counterclinic.walkinappointment.model;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.model.PreferredLanguage;
import com.codesvenue.counterclinic.user.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@Log4j
public class WalkInAppointmentWrapper {

    private WalkInAppointment walkInAppointment;
    private User appointedDoctor;
    private QRCode qrCode;
    private AppointmentStatus appointmentStatus;

    public static WalkInAppointmentWrapper newInstance() {
        return new WalkInAppointmentWrapper();
    }

    public WalkInAppointmentWrapper walkInAppointment(WalkInAppointment walkInAppointment) {
        this.walkInAppointment = walkInAppointment;
        return this;
    }

    public WalkInAppointmentWrapper appointedDoctor(User appointedDoctor) {
        this.appointedDoctor = appointedDoctor;
        return this;
    }

    public WalkInAppointmentWrapper qrCode(QRCode qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    public WalkInAppointmentWrapper appointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
        return this;
    }

    public static class WalkInAppointmentWrapperRowMapper implements RowMapper<WalkInAppointmentWrapper> {

        public static WalkInAppointmentWrapperRowMapper newInstance() {
            return new WalkInAppointmentWrapperRowMapper();
        }

        @Override
        public WalkInAppointmentWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
            User appointedDoctor = User.newInstance()
                    .userId(resultSet.getInt("user_id"))
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .email(resultSet.getString("email"))
                    .mobile(resultSet.getString("mobile"))
                    .username(resultSet.getString("username"))
                    .roles(User.convertRoleToUserRoleEnum(resultSet.getString("user_roles").split(",")))
                    .preferredLanguage(PreferredLanguage.valueOf(resultSet.getString("preferred_language")))
                    .assignClinicRoomId(resultSet.getInt("assigned_clinic_room"));

            WalkInAppointment walkInAppointment = new WalkInAppointment();
            walkInAppointment.setWalkInAppointmentId(resultSet.getInt("walkin_appointment_id"));
            walkInAppointment.setPatientFirstName(resultSet.getString("patient_first_name"));
            walkInAppointment.setPatientLastName(resultSet.getString("patient_last_name"));
            walkInAppointment.setAppointedDoctorId(resultSet.getInt("appointed_doctor_id"));
            walkInAppointment.setCreatedAt(resultSet.getString("created_at"));

            QRCode qrCode = new QRCode();
            qrCode.setQrCodeId(resultSet.getInt("qrcode_id"));
            qrCode.setAppointmentId(resultSet.getInt("walkin_appointment_id"));
            qrCode.setQrCodeHeight(resultSet.getInt("height"));
            qrCode.setQrCodeWidth(resultSet.getInt("width"));
            qrCode.setQrCodeName(resultSet.getString("image_name"));
            qrCode.setQrCodeFilePath(resultSet.getString("image_file_path"));
            qrCode.setQrCodeUrlPath(resultSet.getString("image_url_path"));
            try {
                qrCode.setQrCodeData(new ObjectMapper().readValue(resultSet.getString("qrcode_data"), Map.class));
            } catch (IOException e) {
                log.error("Error converting json to object.", e);
            }

            return WalkInAppointmentWrapper.newInstance()
                    .walkInAppointment(walkInAppointment)
                    .appointedDoctor(appointedDoctor)
                    .qrCode(qrCode);
        }
    }
}
