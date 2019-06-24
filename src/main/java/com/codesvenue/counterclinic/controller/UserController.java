package com.codesvenue.counterclinic.controller;

import com.codesvenue.counterclinic.user.User;
import com.codesvenue.counterclinic.user.UserConstants;
import com.codesvenue.counterclinic.user.UserRole;
import com.codesvenue.counterclinic.user.UserService;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointmentInfoForm;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/user")
@Log4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-appointment")
    public WalkInAppointment createAppointment(
//            @RequestAttribute(UserConstants.LOGGED_IN_USER) User loggedInUser,
            @RequestBody @Valid WalkInAppointmentInfoForm walkInAppointmentInfoForm, BindingResult bindingResult) {
        log.debug("WalkIn Appointment Info: " + walkInAppointmentInfoForm);
        if(bindingResult.hasErrors()) {
            throw new InputValidationException("Appointment cannot be created.", bindingResult.getFieldErrors());
        }

        //TODO: remove this code
        User loggedInUser = loggedInUser = User.newInstance().firstName("Receptionist").lastName("Paul")
                    .roles(UserRole.RECEPTIONIST).email("receptionist.paul@cc.com").mobile("9999999999")
                    .userId(1);
        // -----||-----
        return userService.createNewWalkInAppointment(loggedInUser, walkInAppointmentInfoForm);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") int userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all/{role}")
    public ResponseEntity<List<User>> getAllDoctors(@PathVariable("role") String role) {
        UserRole userRole = UserRole.valueOf(role.toUpperCase());

        List<User> users = userService.getAllUsers(userRole);

        return ResponseEntity.ok(users);
    }

}
