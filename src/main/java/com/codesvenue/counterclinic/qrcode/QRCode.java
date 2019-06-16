package com.codesvenue.counterclinic.qrcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
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
}
