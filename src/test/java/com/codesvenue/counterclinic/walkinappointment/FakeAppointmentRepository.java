package com.codesvenue.counterclinic.walkinappointment;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.qrcode.QRCodeBuilder;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
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
}
