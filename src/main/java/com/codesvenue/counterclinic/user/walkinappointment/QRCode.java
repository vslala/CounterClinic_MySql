package com.codesvenue.counterclinic.user.walkinappointment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class QRCode {

    private Integer qrCodeId;
    private Integer attachmentId;
    private Integer qrCodeHeight;
    private Integer qrCodeWidth;
    private String qrCodeName;
    private String qrCodeFilePath;
    private Map<String,  Object> qrCodeData;

    public static QRCode newInstance() {
        return new QRCode();
    }

    public QRCode attachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
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

    public QRCode qrCodeData(Map<String, Object> qrCodeData) {
        this.qrCodeData = qrCodeData;
        return this;
    }
}
