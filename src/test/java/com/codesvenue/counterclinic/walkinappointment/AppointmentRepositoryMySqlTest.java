package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.walkinappointment.dao.AppointmentRepository;
import com.codesvenue.counterclinic.walkinappointment.dao.AppointmentRepositoryMySql;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentWithAttachment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointments;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class AppointmentRepositoryMySqlTest {

    private AppointmentRepository appointmentRepository;

    @Before
    public void setup() {
        appointmentRepository = new AppointmentRepositoryMySql(TestData.getNamedParameterJdbcTemplate());
    }

    @Test
    public void itShouldFetchDoctorAppointmentsForToday() {
        int doctorId = 1;
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

    @Test
    public void itShouldFetchLatestAppointmentStatus() {
        Optional<AppointmentStatus> appointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorId(1);
        Assert.assertTrue(appointmentStatus.isPresent());
    }

    @Test
    public void itShouldCreateNewWalkInAppointmentIntoTheDatabase() {
        WalkInAppointment walkInAppointment = new WalkInAppointment();
        walkInAppointment.setAppointedDoctorId(2);
        walkInAppointment.setPatientFirstName("Test Patient");
        walkInAppointment.setPatientLastName("Test Patient");
        walkInAppointment.setAppointmentNumber(1);
        WalkInAppointment newAppointment = appointmentRepository.createNewWalkInAppointment(walkInAppointment);

        Assert.assertNotNull(newAppointment);
        Assert.assertNotNull(newAppointment.getWalkInAppointmentId());
        Assert.assertNotNull(newAppointment.getAppointmentNumber());
        Assert.assertTrue(newAppointment.getAppointmentNumber() > 0);
    }

    @Test
    public void itShouldFetchTodaysLatestAppointmentStatusByDoctorId() {
        Integer doctorId = 21;
        Optional<AppointmentStatus> appointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorIdForToday(doctorId);
        Assert.assertFalse(appointmentStatus.isPresent());
    }

    @Test
    public void itShouldFetchAppointmentStatusListForToday() {
        Integer doctorId = 21;
        List<AppointmentStatus> appointmentStatuses = appointmentRepository.fetchAppointmentStatusListForToday(doctorId);
        Assert.assertTrue(appointmentStatuses.isEmpty());
    }

}
