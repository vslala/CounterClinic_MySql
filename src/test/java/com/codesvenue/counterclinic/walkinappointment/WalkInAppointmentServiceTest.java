package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.walkinappointment.dao.AppointmentRepository;
import com.codesvenue.counterclinic.walkinappointment.dao.AppointmentRepositoryMySql;
import com.codesvenue.counterclinic.walkinappointment.model.*;
import com.codesvenue.counterclinic.walkinappointment.service.EmptyWalkInAppointmentException;
import com.codesvenue.counterclinic.walkinappointment.service.WalkInAppointmentService;
import com.codesvenue.counterclinic.walkinappointment.service.WalkInAppointmentServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class WalkInAppointmentServiceTest {

    private WalkInAppointmentService walkInAppointmentService;

    @Before
    public void setup() {
//        AppointmentRepository appointmentRepository = new FakeAppointmentRepository();
        AppointmentRepository appointmentRepository = new AppointmentRepositoryMySql(TestData.getNamedParameterJdbcTemplate());
        SimpMessagingTemplate simpMessagingTemplate = Mockito.mock(SimpMessagingTemplate.class);
        walkInAppointmentService = new WalkInAppointmentServiceImpl(appointmentRepository, simpMessagingTemplate);
        TestData.appointmentStatusList = new ArrayList<>();
    }

    @Test
    public void itShouldFetchTheAvgWaitingTimeOfThePatientAppointment_DoctorHasNotStartedTakingPatient() {
        Integer appointmentId = 5;
        Integer doctorId = 1;

        AppointmentStatus appointmentStatus = walkInAppointmentService.getAppointmentStatus(appointmentId, doctorId, LocalDateTime.now(ZoneOffset.UTC));
        Assert.assertNotNull(appointmentStatus);
        Assert.assertEquals(60, (int) appointmentStatus.getAvgWaitingTime());
    }

    @Test
    public void fetchAvgWaitingTimeWhenTwoPatientsHaveBeenChecked() {
        Integer doctorId = 1;
        Integer appointmentId = 5;

        AppointmentStatus appointmentStatus1 = TestData.firstPersonGoesInsideTheDoctorCabin();
        AppointmentStatus appointmentStatus2 = TestData.secondPersonGoesInsideTheDoctorCabin();

        TestData.store(appointmentStatus1);
        TestData.store(appointmentStatus2);

        LocalDateTime inquiryTime = LocalDateTime.of(LocalDate.of(2019, Month.JUNE, 6), LocalTime.of(11, 30));
        AppointmentStatus appointmentStatus = walkInAppointmentService.getAppointmentStatus(appointmentId, doctorId, inquiryTime);
        Assert.assertNotNull(appointmentStatus);
        Assert.assertEquals(45, (int)appointmentStatus.getAvgWaitingTime());
    }

    @Test
    public void itShouldAddTheDoctorBreakTimeToTheAppointmentAvgWaitingTime() {
        Integer doctorId = 1;
        Integer appointmentId = 5;

        AppointmentStatus appointmentStatus1 = TestData.firstPersonGoesInsideTheDoctorCabin();
        AppointmentStatus appointmentStatus2 = TestData.secondPersonGoesInsideTheDoctorCabin();

        TestData.store(appointmentStatus1);
        TestData.store(appointmentStatus2);
        // have to update the doctor break time in the last appointment of the queue
        TestData.appointmentStatusList.get(TestData.appointmentStatusList.size()-1).setDoctorBreakDuration(10);

        LocalDateTime inquiryTime = LocalDateTime.of(LocalDate.of(2019, Month.JUNE, 6), LocalTime.of(11, 30));
        AppointmentStatus appointmentStatus = walkInAppointmentService.getAppointmentStatus(appointmentId, doctorId, inquiryTime);
        Assert.assertNotNull(appointmentStatus);
        Assert.assertEquals(55, (int)appointmentStatus.getAvgWaitingTime());
    }

    @Test
    public void itShouldNotifyReceptionAndCreateNewAppointmentStatus() {
        User user = User.newInstance().roles(UserRole.DOCTOR).userId(1);
        AppointmentStatus newAppointmentStatus = walkInAppointmentService.callNextPatient(user);
        Assert.assertNotNull(newAppointmentStatus);
//        Assert.assertEquals(1, (int) newAppointmentStatus.getCurrentAppointmentId());
        Assert.assertEquals(15, (int) newAppointmentStatus.getCurrentAppointmentId());

//        newAppointmentStatus = walkInAppointmentService.callNextPatient(user);
//        Assert.assertEquals(16, (int) newAppointmentStatus.getCurrentAppointmentId());
//        Assert.assertEquals(2, (int) newAppointmentStatus.getCurrentAppointmentId());
    }

    @Test
    public void itShouldFetchAllWalkInAppointments() {
        WalkInAppointments walkInAppointments = walkInAppointmentService.getAllAppointments();
        Assert.assertNotNull(walkInAppointments);
        Assert.assertFalse(walkInAppointments.getWalkInAppointmentList().isEmpty());
    }

    @Test
    public void itShouldDeleteAppointmentByAppointmentId() {
        boolean isDeleteSuccessful = walkInAppointmentService.deleteAppointment(15);
        Assert.assertTrue(isDeleteSuccessful);
    }

    @Test
    public void itShouldGetQRCodeByAppointmentId() {
        QRCode qrCode = walkInAppointmentService.getQrCodeForAppointment(1);
        Assert.assertNotNull(qrCode);
    }

    @Test
    public void itShouldFetchAppointmentWithAttachment() {
        List<WalkInAppointmentWithAttachment> walkInAppointmentWithAttachment = walkInAppointmentService.getAllWalkInAppointmentWithAttachment();
        Assert.assertNotNull(walkInAppointmentWithAttachment);
        Assert.assertFalse(walkInAppointmentWithAttachment.isEmpty());
    }

    @Test
    public void itShouldFetchAppointmentWrapperByAppointmentId() {
        WalkInAppointmentWrapper walkInAppointmentWrapper = walkInAppointmentService.getWrappedAppointment(5);
        Assert.assertNotNull(walkInAppointmentWrapper);
        Assert.assertNotNull(walkInAppointmentWrapper.getAppointedDoctor());
        Assert.assertNotNull(walkInAppointmentWrapper.getQrCode());
        Assert.assertNotNull(walkInAppointmentWrapper.getWalkInAppointment());
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void itShouldThrowEmptyWalkInAppointmentIfNoAppointmentIsPresentForTheDay() {
        expectedException.expect(EmptyWalkInAppointmentException.class);
        User user = User.newInstance().userId(1).roles(UserRole.DOCTOR);
        WalkInAppointment walkInAppointment = walkInAppointmentService.getNextAppointment(user);
        Assert.assertNotNull(walkInAppointment);
    }

    @Test
    public void itShouldGetFirstAppointmentFromTheDoctorQueueIfAppointmentStatusIsNull() {
        User user = User.newInstance().userId(1).roles(UserRole.DOCTOR);
        WalkInAppointment walkInAppointment = walkInAppointmentService.getNextAppointment(user);
        Assert.assertNotNull(walkInAppointment);
        Assert.assertEquals(15, walkInAppointment.getWalkInAppointmentId().intValue());
    }

    @Test
    public void itShouldInsertANewAppointmentStatusWhenDoctorTakesABreak() {
        User user = User.newInstance().userId(1).roles(UserRole.DOCTOR);
        int breakDuration = 23;
        AppointmentStatus appointmentStatus = walkInAppointmentService.doctorTakesBreak(user, breakDuration);
        Assert.assertNotNull(appointmentStatus);
        Assert.assertEquals(23, appointmentStatus.getDoctorBreakDuration().intValue());
    }
}
