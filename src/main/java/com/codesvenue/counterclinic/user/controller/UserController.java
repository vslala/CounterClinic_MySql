package com.codesvenue.counterclinic.user.controller;

import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserLogin;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.user.service.UserService;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentInfoForm;
import lombok.Value;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
@Log4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register-user")
    public ResponseEntity registerUser(@Valid @RequestBody User user, BindingResult binding) {
        if (binding.hasErrors()) {
            StringBuilder sb = new StringBuilder("Errors:").append(System.lineSeparator());
            binding.getFieldErrors().forEach(fe -> sb.append(fe.getDefaultMessage()).append(System.lineSeparator()));
            log.warn("Invalid input for user reigistration." + sb.toString());
            return ResponseEntity.badRequest().body("Invalid user object." + sb.toString());
        }

        UserLogin newUserLogin = userService.createNewUser(user);
        return ResponseEntity.ok(newUserLogin);

    }

    @PatchMapping("/update-user")
    public ResponseEntity updateUser(@Valid @RequestBody User user, BindingResult binding) {
//        if (binding.hasErrors()) {
//            StringBuilder sb = new StringBuilder("Errors:").append(System.lineSeparator());
//            binding.getFieldErrors().forEach(fe -> sb.append(fe.getDefaultMessage()).append(System.lineSeparator()));
//            log.warn("Invalid input for user registration. " + sb.toString());
//            return ResponseEntity.badRequest().body("Invalid user object." + sb.toString());
//        }

        User updatedUser = userService.updateUser(user);

        return ResponseEntity.ok(updatedUser);
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
    public ResponseEntity<List<User>> getAllUsersByRole(@PathVariable("role") String role) {
        UserRole userRole = UserRole.valueOf(role.toUpperCase());

        List<User> users = userService.getAllUsers(userRole);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/roles")
    public ResponseEntity<UserRole[]> getUserRoles() {
        return ResponseEntity.ok(UserRole.values());
    }

}
