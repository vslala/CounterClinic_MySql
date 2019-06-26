package com.codesvenue.counterclinic.walkinappointment.controller;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentWrapper;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointments;
import com.codesvenue.counterclinic.walkinappointment.service.WalkInAppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/walk-in-appointment")
public class WalkInAppointmentController {

    private WalkInAppointmentService walkInAppointmentService;

    public WalkInAppointmentController(WalkInAppointmentService walkInAppointmentService) {
        this.walkInAppointmentService = walkInAppointmentService;
    }

    @GetMapping("/all")
    public ResponseEntity<WalkInAppointments> getAllWalkInAppointments() {
        WalkInAppointments walkInAppointments = walkInAppointmentService.getAllAppointments();
        return ResponseEntity.ok(walkInAppointments);
    }

    @DeleteMapping("/id/{appointmentId}")
    public ResponseEntity<Boolean> deleteAppointmentById(@PathVariable("appointmentId") int appointmentId) {
        boolean isDeleteSuccessful = walkInAppointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.ok(isDeleteSuccessful);
    }

    @GetMapping("/wrapped/id/{appointmentId}")
    public ResponseEntity<WalkInAppointmentWrapper> getAppointmentWrapper(@PathVariable("appointmentId") int appointmentId) {
        WalkInAppointmentWrapper walkInAppointmentWrapper = walkInAppointmentService.getWrappedAppointment(appointmentId);
        return ResponseEntity.ok(walkInAppointmentWrapper);
    }

    @GetMapping("/{appointmentId}/qrcode")
    public ResponseEntity<QRCode> getQrCodeForAppointment(@PathVariable("appointmentId") int appointmentId) {
        return ResponseEntity.ok(walkInAppointmentService.getQrCodeForAppointment(appointmentId));
    }



    @GetMapping("/call-next-patient")
    public ResponseEntity<AppointmentStatus> callNextPatient() {
        User user = User.newInstance().userId(1).roles(UserRole.DOCTOR);
        AppointmentStatus appointmentStatus = walkInAppointmentService.callNextPatient(user);
        return ResponseEntity.ok(appointmentStatus);
    }

    @GetMapping("/appointment-status/latest")
    public ResponseEntity<AppointmentStatus> getLatestAppointmentStatus() {
        User user = User.newInstance().userId(1).roles(UserRole.DOCTOR);
        AppointmentStatus appointmentStatus = walkInAppointmentService.getLatestAppointmentStatus(user);
        return ResponseEntity.ok(appointmentStatus);
    }
}
