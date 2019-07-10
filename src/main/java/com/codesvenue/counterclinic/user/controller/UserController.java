package com.codesvenue.counterclinic.user.controller;

import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserLogin;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.user.service.UserService;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentInfoForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "create new user into the database", response = UserLogin.class)
    @PostMapping("/register-user")
    public ResponseEntity registerUser(@Valid @RequestBody User user) {
        log.debug("User Object: " + user);
        UserLogin newUserLogin = userService.createNewUser(user);
        return ResponseEntity.ok(newUserLogin);

    }

    @ApiOperation(value = "updates the user into the database", notes = "Pass the entire use object along with roles to update", response = User.class)
    @PatchMapping("/update-user")
    public ResponseEntity updateUser(@Valid @RequestBody User user) {
        log.debug("User Object: " + user);
        User updatedUser = userService.updateUser(user);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable("userId") int userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @ApiOperation(value = "creates new appointment into the database", response = WalkInAppointment.class)
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

    @ApiOperation(value = "fetches user object by id", response = User.class)
    @GetMapping("/get/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") int userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "fetches all users by the given role", response = User[].class)
    @GetMapping("/all/{role}")
    public ResponseEntity<List<User>> getAllUsersByRole(@PathVariable("role") String role) {
        UserRole userRole = UserRole.valueOf(role.toUpperCase());

        List<User> users = userService.getAllUsers(userRole);

        return ResponseEntity.ok(users);
    }

    @ApiOperation(value = "fetches all users from the database", response = User[].class)
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @ApiOperation(value = "fetches all the roles present in the database", response = UserRole[].class)
    @GetMapping("/roles")
    public ResponseEntity<UserRole[]> getUserRoles() {
        return ResponseEntity.ok(UserRole.values());
    }

}
