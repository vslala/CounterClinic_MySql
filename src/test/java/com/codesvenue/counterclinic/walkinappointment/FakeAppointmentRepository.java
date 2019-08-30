package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.qrcode.QRCodeBuilder;
import com.codesvenue.counterclinic.walkinappointment.dao.AppointmentRepository;
import com.codesvenue.counterclinic.walkinappointment.model.*;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Log4j
public class FakeAppointmentRepository implements AppointmentRepository {
    @Override
    public List<WalkInAppointment> fetchDoctorAppointmentsForToday(Integer doctorId) {
        return TestData.buildWalkInAppointments(10, 15);
    }

    @Override
    public List<AppointmentStatus> fetchAppointmentStatusList(Integer doctorId) {
        return TestData.appointmentStatusList;
    }

    @Override
    public List<AppointmentStatus> fetchAppointmentStatusListForToday(Integer doctorId) {
        return Collections.emptyList();
    }

    @Override
    public AppointmentStatus saveAppointmentStatus(AppointmentStatus appointmentStatus) {
        appointmentStatus.setAppointmentStatusId(TestData.appointmentStatusList.size()+1);
        TestData.store(appointmentStatus);
        return appointmentStatus;
    }

    @Override
    public WalkInAppointments fetchAllWalkInAppointments() {
        return null;
    }

    @Override
    public int deleteWalkInAppointment(int appointmentId) {
        return 1;
    }

    @Override
    public QRCode deleteQrCodeAttachment(int appointmentId) {
        Map<String, Object> data = new HashMap<>();
        data.put("appointmentId", 5);
        data.put("doctorId", 1);
        return QRCodeBuilder.newInstance().build(1, data);
    }

    @Override
    public QRCode fetchQrCodeAttachment(int appointmentId) {
        Map<String, Object> data = new HashMap<>();
        data.put("appointmentId", 5);
        data.put("doctorId", 1);
        return QRCodeBuilder.newInstance().build(1, data);
    }

    @Override
    public List<WalkInAppointmentWithAttachment> fetchAllWalkInAppointmentsWithAttachments() {
        return null;
    }

    @Override
    public WalkInAppointmentWrapper findWalkInAppointmentById(int appointmentId) {
        return null;
    }

    @Override
    public Optional<AppointmentStatus> findLatestAppointmentStatusByDoctorId(Integer userId) {
        return Optional.empty();
    }

    @Override
    public Optional<AppointmentStatus> findLatestAppointmentStatusByDoctorIdForToday(Integer doctorId) {
        return Optional.empty();
    }

    @Override
    public WalkInAppointment createNewWalkInAppointment(WalkInAppointment walkInAppointment) {
        return walkInAppointment;
    }

    @Override
    public QRCode createNewQRCode(QRCode qrCode) {
        return null;
    }
}
