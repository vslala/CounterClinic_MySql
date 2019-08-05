package com.codesvenue.counterclinic.qrcode;

import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
@Log4j
public class QRCode {

    private Integer qrCodeId;
    private Integer appointmentId;
    private Integer qrCodeHeight;
    private Integer qrCodeWidth;
    private String qrCodeName;
    private String qrCodeFilePath;
    private String qrCodeUrlPath;
    private Map<String,  Object> qrCodeData;

    private QRCode(QRCode other) {
        this.qrCodeId = other.qrCodeId;
        this.appointmentId = other.appointmentId;
        this.qrCodeHeight = other.qrCodeHeight;
        this.qrCodeWidth = other.qrCodeWidth;
        this.qrCodeName = other.qrCodeName;
        this.qrCodeFilePath = other.qrCodeFilePath;
        this.qrCodeUrlPath = other.qrCodeUrlPath;
        this.qrCodeData = other.qrCodeData;
    }

    public static QRCode newInstance() {
        return new QRCode();
    }

    public static QRCode copyInstance(QRCode qrCode) {
        return new QRCode(qrCode);
    }

    public QRCode qrCodeId(Integer qrCodeId) {
        this.qrCodeId = qrCodeId;
        return this;
    }

    public QRCode appointmentId(Integer attachmentId) {
        this.appointmentId = attachmentId;
        return this;
    }

    public QRCode qrCodeHeight(Integer height) {
        this.qrCodeHeight = height;
        return this;
    }

    public QRCode qrCodeWidth(Integer width) {
        this.qrCodeWidth = width;
        return this;
    }

    public QRCode qrCodeName(String name) {
        this.qrCodeName = name;
        return this;
    }

    public QRCode filePath(String filePath) {
        this.qrCodeFilePath = filePath;
        return this;
    }

    public QRCode qrCodeUrlPath(String url) {
        this.qrCodeUrlPath = url;
        return this;
    }

    public QRCode qrCodeData(Map<String, Object> qrCodeData) {
        this.qrCodeData = qrCodeData;
        return this;
    }

    public String getQrCodeDataInJson() throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(qrCodeData);
    }

    @Component
    public static class Generator {

        private static String qrCodeFolder;
        private static String qrCodeUrlPath;

        @Value("${qrcode.folder.path}")
        public void setQRCodeFolder(String qrCodeFolder) {
            Generator.qrCodeFolder = qrCodeFolder;
        }

        @Value("${qrcode.url.path}")
        public void setQRCodeUrlPath(String qrCodeUrlPath) {
            Generator.qrCodeUrlPath = qrCodeUrlPath;
        }


        public static QRCode generateQRCode(final WalkInAppointment newWalkInAppointment) {
            Map<String, Object> qrCodeData = new HashMap<>();
            qrCodeData.put("appointmentId", newWalkInAppointment.getWalkInAppointmentId());
            qrCodeData.put("appointedDoctorId", newWalkInAppointment.getAppointedDoctorId());
            long now = System.currentTimeMillis();
            String qrCodeFilePath = String.format("%s/%s.png", Generator.qrCodeFolder, now);
            String url = String.format("%s/%s.png", Generator.qrCodeUrlPath, now);

            QRCode qrCode = QRCodeBuilder.newInstance()
                    .filePath(qrCodeFilePath).url(url).build(
                            newWalkInAppointment.getWalkInAppointmentId(),
                            qrCodeData
                    );

            log.info("QRCode Generated Successfully!");
            log.info("QRCode Data: " + qrCodeData);
            log.info("QRCode File Path: " + qrCodeFilePath);
            return qrCode;
        }
    }

    @Log4j
    public static class QRCodeRowMapper implements RowMapper<QRCode> {

        public static QRCodeRowMapper newInstance() {
            return new QRCodeRowMapper();
        }

        @Override
        public QRCode mapRow(ResultSet resultSet, int i) throws SQLException {
            QRCode qrCode = new QRCode();
            qrCode.setQrCodeId(resultSet.getInt("qrcode_id"));
            qrCode.setAppointmentId(resultSet.getInt("appointment_id"));
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
            return qrCode;
        }
    }
}
