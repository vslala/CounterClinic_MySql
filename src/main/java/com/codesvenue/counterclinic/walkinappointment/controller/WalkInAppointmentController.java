package com.codesvenue.counterclinic.walkinappointment.controller;

import com.codesvenue.counterclinic.clinic.model.FetchAppointmentInfoQuery;
import com.codesvenue.counterclinic.configuration.DateTimeConstants;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.UserConstants;
import com.codesvenue.counterclinic.user.controller.InputValidationException;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.walkinappointment.model.*;
import com.codesvenue.counterclinic.walkinappointment.service.WalkInAppointmentService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/walk-in-appointment")
@Log4j
public class WalkInAppointmentController {

    private WalkInAppointmentService walkInAppointmentService;

    public WalkInAppointmentController(WalkInAppointmentService walkInAppointmentService) {
        this.walkInAppointmentService = walkInAppointmentService;
    }

    @ApiOperation(value = "creates new appointment into the database", response = WalkInAppointment.class)
    @PostMapping("/create-appointment")
    public WalkInAppointment createAppointment(
            @RequestAttribute(UserConstants.LOGGED_IN_USER) User loggedInUser,
            @RequestBody @Valid WalkInAppointmentInfoForm walkInAppointmentInfoForm, BindingResult bindingResult) {
        log.debug("WalkIn Appointment Info: " + walkInAppointmentInfoForm);
        if(bindingResult.hasErrors()) {
            throw new InputValidationException("Appointment cannot be created.", bindingResult.getFieldErrors());
        }

        return walkInAppointmentService.createNewWalkInAppointment(loggedInUser, walkInAppointmentInfoForm);
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

    @GetMapping("/qrcode/{appointmentId}")
    public ResponseEntity<QRCode> getQrCodeForAppointment(@PathVariable("appointmentId") int appointmentId) {
        return ResponseEntity.ok(walkInAppointmentService.getQrCodeForAppointment(appointmentId));
    }



    @GetMapping("/call-next-patient")
    public ResponseEntity<AppointmentStatus> callNextPatient(@RequestAttribute(UserConstants.LOGGED_IN_USER) User user) {
        AppointmentStatus appointmentStatus = walkInAppointmentService.callNextPatient(user);
        return ResponseEntity.ok(appointmentStatus);
    }

    @GetMapping("/appointment-status/latest")
    public ResponseEntity<AppointmentStatus> getLatestAppointmentStatus(@RequestAttribute(UserConstants.LOGGED_IN_USER) User loggedInUser) {
        AppointmentStatus appointmentStatus = walkInAppointmentService.getLatestAppointmentStatus(loggedInUser);
        return ResponseEntity.ok(appointmentStatus);
    }

    @GetMapping("/appointment-status/latest/{doctorId}")
    public ResponseEntity<AppointmentStatus> getLatestAppointmentStatusByDoctorId(@PathVariable("doctorId") Integer doctorId, @PathVariable(name = "day", required = false) String day) {
        AppointmentStatus latestAppointmentStatus = walkInAppointmentService.getLatestAppointmentStatus(new User().userId(doctorId), day);
        return ResponseEntity.ok(latestAppointmentStatus);
    }

    @GetMapping("/appointment-status")
    public ResponseEntity<AppointmentStatus> getLatestAppointmentStatusByDoctorId(@Valid  @ModelAttribute("fetchAppointmentInfoQuery") FetchAppointmentInfoQuery fetchAppointmentInfoQuery) {
        String inquiryTime = Objects.isNull(fetchAppointmentInfoQuery.getInquiryDateTime()) ?
                LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)) :
                fetchAppointmentInfoQuery.getInquiryDateTime();
        AppointmentStatus appointmentStatus = walkInAppointmentService.getAppointmentStatus(fetchAppointmentInfoQuery.getAppointmentId(),
                fetchAppointmentInfoQuery.getDoctorId(),
                LocalDateTime.parse(inquiryTime,
                        DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)));
        return ResponseEntity.ok(appointmentStatus);
    }

    @GetMapping("/take-break")
    public ResponseEntity<AppointmentStatus> takeBreak(@RequestAttribute(UserConstants.LOGGED_IN_USER) User loggedInUser,
                                                       @RequestParam("breakDuration") int breakDuration) {
        AppointmentStatus appointmentStatus = walkInAppointmentService.doctorTakesBreak(loggedInUser, breakDuration);
        return ResponseEntity.ok(appointmentStatus);
    }
}
