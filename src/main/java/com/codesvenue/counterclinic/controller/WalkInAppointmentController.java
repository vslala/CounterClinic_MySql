package com.codesvenue.counterclinic.controller;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointmentService;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointmentWrapper;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointments;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}
