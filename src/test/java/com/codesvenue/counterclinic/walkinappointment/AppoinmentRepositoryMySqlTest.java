package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.qrcode.QRCode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class AppoinmentRepositoryMySqlTest {

    private AppointmentRepository appointmentRepository;

    @Before
    public void setup() {
        appointmentRepository = new AppointmentRepositoryMySql(TestData.getNamedParameterJdbcTemplate());
    }

    @Test
    public void itShouldFetchDoctorAppointmentsForToday() {
        Integer doctorId = 1;
        List<WalkInAppointment> walkInAppointmentList = appointmentRepository.fetchDoctorAppointmentsForToday(doctorId);
        Assert.assertNotNull(walkInAppointmentList);
        Assert.assertFalse(walkInAppointmentList.isEmpty());
    }

    @Test
    public void itShouldFetchAllWalkInAppointments() {
        WalkInAppointments walkInAppointments = appointmentRepository.fetchAllWalkInAppointments();
        Assert.assertNotNull(walkInAppointments);
        Assert.assertNotNull(walkInAppointments.getWalkInAppointmentList().get(0).getCreatedAt());
    }

    @Test
    public void itShouldDeleteAppointmentById() {
        int rowsAffected = appointmentRepository.deleteWalkInAppointment(12);
        Assert.assertEquals(1, rowsAffected);
    }

    @Test
    public void itShouldFetchQRCodeAttachmentByAppointmentId() {
        QRCode qrCode = appointmentRepository.fetchQrCodeAttachment(12);
        Assert.assertNotNull(qrCode);
        Assert.assertNotNull(qrCode.getQrCodeFilePath());
        System.out.println(qrCode);
    }

    @Test
    public void itShouldDeleteQRCodeAttachmentForTheAppointmentId() {
        QRCode qrCode = appointmentRepository.deleteQrCodeAttachment(1);
        Assert.assertNotNull(qrCode);
    }

    @Test
    public void itShouldFetchAppointmentWithAttachments() {
        List<WalkInAppointmentWithAttachment> wrapper = appointmentRepository.fetchAllWalkInAppointmentsWithAttachments();
        Assert.assertNotNull(wrapper.get(0).getWalkInAppointment());
        Assert.assertNotNull(wrapper.get(0).getAttachedQRCode());
    }
}
