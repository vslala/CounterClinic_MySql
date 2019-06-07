package com.codesvenue.counterclinic.user.walkinappointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

@Log4j
public class QRCodeBuilder {
    private int width;
    private int height;
    private String format;
    private String filePath;

    private QRCodeBuilder() {
        // setting default
        width = 300;
        height = 300;
        format = "png";
        filePath = "src/main/resources/qrcode/" + System.currentTimeMillis() + "." + format;
    }

    public static QRCodeBuilder newInstance() {
        return new QRCodeBuilder();
    }

    public QRCodeBuilder height(int height) {
        this.height = height;
        return this;
    }

    public QRCodeBuilder width(int width) {
        this.width = width;
        return this;
    }

    public QRCodeBuilder filePath(String filePath) {
        validateExtensionWhitelist(filePath);
        this.filePath = filePath;
        return this;
    }

    private void validateExtensionWhitelist(String filePath) {
        String[] allowedExtensions = new String[]{"png", "jpeg", "jpg", "PNG", "JPEG", "JPG"};
        String extension = FilenameUtils.getExtension(filePath);
        if (! Arrays.asList(allowedExtensions).contains(extension))
            throw new QRCodeException(String.format("File Extension [%s] not allowed.", extension));
    }

    public QRCodeBuilder format(String format) {
        this.format = format;
        return this;
    }

    public QRCode build(Integer attachmentId, Map<String, Object> qrCodeInput) {
        generateDirectoryStructure();
        generateQRCode(qrCodeInput);
        return QRCode.newInstance()
                .attachmentId(attachmentId)
                .qrCodeHeight(height)
                .qrCodeWidth(width)
                .filePath(filePath)
                .qrCodeName(FilenameUtils.getName(filePath))
                .qrCodeData(qrCodeInput);
    }

    private void generateDirectoryStructure() {
        File file = Paths.get(filePath).toFile();
        if (! file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    private void generateQRCode(Map<String, Object> qrCodeInput) {
        try {
            log.info("Building QRCode");
            String jsonData = new ObjectMapper().writeValueAsString(qrCodeInput);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(jsonData, BarcodeFormat.QR_CODE, this.width, this.height);
            MatrixToImageWriter.writeToPath(bitMatrix, "png", Paths.get(this.filePath));
        } catch (WriterException | IOException e) {
            log.error("Error while creating QRCode. Error: " + e.getMessage(), e);
            throw new QRCodeException("Error while trying to build qr code: " + e.getMessage());
        }
    }


}
