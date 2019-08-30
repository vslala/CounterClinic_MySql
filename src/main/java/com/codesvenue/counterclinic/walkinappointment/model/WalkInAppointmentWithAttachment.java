package com.codesvenue.counterclinic.walkinappointment.model;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Data
@NoArgsConstructor
@Log4j
public class WalkInAppointmentWithAttachment {
    private WalkInAppointment walkInAppointment;
    private QRCode attachedQRCode;

    public static class WalkInAppointmentWithAttachmentRowMapper implements RowMapper<WalkInAppointmentWithAttachment> {

        public static WalkInAppointmentWithAttachmentRowMapper newInstance() {
            return new WalkInAppointmentWithAttachmentRowMapper();
        }


        @Override
        public WalkInAppointmentWithAttachment mapRow(ResultSet resultSet, int i) throws SQLException {
            WalkInAppointmentWithAttachment walkInAppointmentWithAttachment = new WalkInAppointmentWithAttachment();

            WalkInAppointment walkInAppointment = new WalkInAppointment();
            walkInAppointment.setWalkInAppointmentId(resultSet.getInt("walkin_appointment_id"));
            walkInAppointment.setPatientFirstName(resultSet.getString("patient_first_name"));
            walkInAppointment.setPatientLastName(resultSet.getString("patient_last_name"));
            walkInAppointment.setAppointedDoctorId(resultSet.getInt("appointed_doctor_id"));
            walkInAppointment.setAppointmentNumber(resultSet.getInt("appointment_number"));
            walkInAppointment.setCreatedAt(resultSet.getString("created_at"));
            walkInAppointmentWithAttachment.setWalkInAppointment(walkInAppointment);

            log.debug("WalkInAppointment: " + walkInAppointment);

            QRCode qrCode = new QRCode();
            qrCode.setQrCodeId(resultSet.getInt("qrcode_id"));
            qrCode.setAppointmentId(resultSet.getInt("walkin_appointment_id"));
            qrCode.setQrCodeHeight(resultSet.getInt("height"));
            qrCode.setQrCodeWidth(resultSet.getInt("width"));
            qrCode.setQrCodeName(resultSet.getString("image_name"));
            qrCode.setQrCodeFilePath(resultSet.getString("image_file_path"));
            qrCode.setQrCodeUrlPath(resultSet.getString("image_url_path"));
            log.debug("QRCode: " + qrCode);
            try {
                qrCode.setQrCodeData(new ObjectMapper().readValue(
                        resultSet.getString("qrcode_data"), Map.class));
            } catch (IOException e) {
                log.error("Error converting json to object.", e);
            }
            walkInAppointmentWithAttachment.setAttachedQRCode(qrCode);
            return walkInAppointmentWithAttachment;
        }
    }
}
