package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.walkinappointment.dao.AppointmentRepository;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentWithAttachment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointments;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class AppointmentRepositoryMySqlTest {
    @Autowired
    DataSource dataSource;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private AppointmentRepository appointmentRepository;


    @Before
    public void setup() {
//        jdbcTemplate = new FakeJdbcTemplate(dataSource);
//        appointmentRepository = new AppointmentRepositoryMySql(jdbcTemplate);
    }

    @Test
    public void itShouldCreateNewWalkInAppointmentIntoTheDatabase() {
        WalkInAppointment walkInAppointment = new WalkInAppointment();
        walkInAppointment.setAppointedDoctorId(1);
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
    public void itShouldFetchDoctorAppointmentsForToday() throws SQLException {
        // Arrange
        int doctorId = 1;

        // Act
        List<WalkInAppointment> walkInAppointmentList = appointmentRepository.fetchDoctorAppointmentsForToday(doctorId);

        // Asserts
        Assert.assertNotNull(walkInAppointmentList);
        Assert.assertFalse(walkInAppointmentList.isEmpty());
    }

    @Test
    public void itShouldFetchAllWalkInAppointments() {
        // Arrange
        WalkInAppointments testReturn = new WalkInAppointments();

        // Act
        WalkInAppointments walkInAppointments = appointmentRepository.fetchAllWalkInAppointments();

        // Asserts
        Assert.assertNotNull(walkInAppointments);
        Assert.assertNotNull(walkInAppointments.getWalkInAppointmentList().get(0).getCreatedAt());
    }

    @Test
    public void itShouldFetchLatestAppointmentStatus() {
        Optional<AppointmentStatus> appointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorId(1);
        Assert.assertTrue(appointmentStatus.isPresent());
    }

    @Test
    public void itShouldFetchTodaysLatestAppointmentStatusByDoctorId() {
        Integer doctorId = 1;
        Optional<AppointmentStatus> appointmentStatus = appointmentRepository.findLatestAppointmentStatusByDoctorIdForToday(doctorId);
        Assert.assertTrue(appointmentStatus.isPresent());
    }

    @Test
    public void itShouldFetchAppointmentStatusListForToday() {
        Integer doctorId = 1;
        List<AppointmentStatus> appointmentStatuses = appointmentRepository.fetchAppointmentStatusListForToday(doctorId);
        Assert.assertFalse(appointmentStatuses.isEmpty());
    }

    @Test
    public void itShouldDeleteAppointmentById() {
        int rowsAffected = appointmentRepository.deleteWalkInAppointment(2);
        Assert.assertEquals(1, rowsAffected);
    }

    @Test
    public void itShouldFetchQRCodeAttachmentByAppointmentId() {
        QRCode qrCode = appointmentRepository.fetchQrCodeAttachment(3);
        System.out.println(qrCode);
        Assert.assertNotNull(qrCode);
        Assert.assertNotNull(qrCode.getQrCodeFilePath());
    }

    @Test
    public void itShouldDeleteQRCodeAttachmentForTheAppointmentId() {
        QRCode qrCode = appointmentRepository.deleteQrCodeAttachment(2);
        Assert.assertNotNull(qrCode);
    }

    @Ignore
    @Test
    public void itShouldFetchAppointmentWithAttachments() {
        List<WalkInAppointmentWithAttachment> wrapper = appointmentRepository.fetchAllWalkInAppointmentsWithAttachments();
        Assert.assertNotNull(wrapper.get(0).getWalkInAppointment());
        Assert.assertNotNull(wrapper.get(0).getAttachedQRCode());
    }

}
