package com.codesvenue.counterclinic.controller;

import com.codesvenue.counterclinic.user.User;
import com.codesvenue.counterclinic.user.UserConstants;
import com.codesvenue.counterclinic.user.UserService;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointmentInfoForm;
import lombok.extern.log4j.Log4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Log4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/create-appointment")
    public WalkInAppointment createAppointment(@RequestAttribute(UserConstants.LOGGED_IN_USER) User loggedInUser,
            @RequestBody @Valid WalkInAppointmentInfoForm walkInAppointmentInfoForm, BindingResult bindingResult) {
        log.debug("WalkIn Appointment Info: " + walkInAppointmentInfoForm);
        if(bindingResult.hasErrors()) {
            throw new InputValidationException("Appointment cannot be created.", bindingResult.getFieldErrors());
        }
        return userService.createNewWalkInAppointment(loggedInUser, walkInAppointmentInfoForm);
    }
}
