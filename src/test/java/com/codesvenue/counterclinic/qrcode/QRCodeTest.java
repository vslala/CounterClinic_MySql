package com.codesvenue.counterclinic.qrcode;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.qrcode.QRCodeBuilder;
import com.codesvenue.counterclinic.qrcode.QRCodeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

public class QRCodeTest {

    @Before
    public void setup() {
    }

    @Test
    public void createQRCodeByConvertingObjectIntoJsonFormat() {
        Integer attachmentId = 2;

        Map<String, Object> qrCodeInput = new HashMap<>();
        qrCodeInput.put("appointmentId", attachmentId);
        qrCodeInput.put("doctorId", "5");
        QRCode qrCode = QRCodeBuilder.newInstance()
                .filePath("src/test/resources/qrcode/testqr.png")
                .build(attachmentId, qrCodeInput);
        Assert.assertNotNull(qrCode);
        Assert.assertNotNull(qrCode.getQrCodeData());
        Assert.assertNotNull(qrCode.getQrCodeFilePath());
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void itShouldOnlyAllowJPEGAndPNGFormatElseThrowQRCodeException() {
        expectedException.expect(QRCodeException.class);

        Integer attachmentId = 2;

        Map<String, Object> qrCodeInput = new HashMap<>();
        qrCodeInput.put("appointmentId", attachmentId);
        qrCodeInput.put("doctorId", "5");
        QRCode qrCode = QRCodeBuilder.newInstance()
                .filePath("src/test/resources/qrcode/testqr.pdf")
                .build(attachmentId, qrCodeInput);

        Assert.assertNotNull(qrCode);
        Assert.assertNotNull(qrCode.getQrCodeData());
        Assert.assertNotNull(qrCode.getQrCodeFilePath());
    }
}
