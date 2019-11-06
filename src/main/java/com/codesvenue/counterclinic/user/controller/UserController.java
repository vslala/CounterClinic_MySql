package com.codesvenue.counterclinic.user.controller;

import com.codesvenue.counterclinic.clinic.model.Response;
import com.codesvenue.counterclinic.setting.model.Setting;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserLogin;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Response<UserLogin>> registerUser(@Valid @RequestBody User user) {
        log.debug("User Object: " + user);
        UserLogin newUserLogin = userService.createNewUser(user);
        return ResponseEntity.ok(Response.newInstance(newUserLogin));

    }

    @ApiOperation(value = "updates the user into the database", notes = "Pass the entire use object along with roles to update", response = User.class)
    @PatchMapping("/update-user")
    public ResponseEntity<Response<User>> updateUser(@Valid @RequestBody User user) {
        log.debug("User Object: " + user);
        User updatedUser = userService.updateUser(user);

        return ResponseEntity.ok(Response.newInstance(updatedUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Response<Boolean>> deleteUser(@PathVariable("userId") int userId){
        return ResponseEntity.ok(Response.newInstance(userService.deleteUser(userId)));
    }

    @ApiOperation(value = "fetches user object by id", response = User.class)
    @GetMapping("/get/{userId}")
    public ResponseEntity<Response<User>> getUserById(@PathVariable("userId") int userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(Response.newInstance(user));
    }

    @ApiOperation(value = "fetches all users by the given role", response = User[].class)
    @GetMapping("/all/{role}")
    public ResponseEntity<Response<List<User>>> getAllUsersByRole(@PathVariable("role") String role) {
        UserRole userRole = UserRole.valueOf(role.toUpperCase());

        List<User> users = userService.getAllUsers(userRole);

        return ResponseEntity.ok(Response.newInstance(users));
    }

    @ApiOperation(value = "fetches all users from the database", response = User[].class)
    @GetMapping("/all")
    public ResponseEntity<Response<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(Response.newInstance(users));
    }

    @ApiOperation(value = "fetches all the roles present in the database", response = UserRole[].class)
    @GetMapping("/roles")
    public ResponseEntity<Response<UserRole[]>> getUserRoles() {
        return ResponseEntity.ok(Response.newInstance(UserRole.values()));
    }

    @PostMapping("/file-upload")
    public ResponseEntity<Response<Setting>> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("attachmentType") String attachmentType) {
        log.debug("File to upload: " + file.getOriginalFilename());
        log.debug("File to upload size: " + file.getSize());
        Setting uploadedFileSetting = userService.uploadFile(file, attachmentType);
        return ResponseEntity.ok(Response.newInstance(uploadedFileSetting));
    }

}
