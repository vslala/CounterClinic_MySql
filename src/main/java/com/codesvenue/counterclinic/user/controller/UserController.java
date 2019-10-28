package com.codesvenue.counterclinic.user.controller;

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

    @PostMapping("/file-upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("attachmentType") String attachmentType) {
        log.debug("File to upload: " + file.getOriginalFilename());
        log.debug("File to upload size: " + file.getSize());
        Setting uploadedFileSetting = userService.uploadFile(file, attachmentType);
        return ResponseEntity.ok(uploadedFileSetting);
    }

    @GetMapping("/setting/{settingName}")
    public ResponseEntity<Setting> getSettingByName(@PathVariable("settingName") String settingName) {
        log.debug("Getting setting for name: " + settingName);
        Setting setting = userService.getSetting(settingName);
        return ResponseEntity.ok(setting);
    }

    @GetMapping("/settings")
    public ResponseEntity<List<Setting>> getAllSettings() {
        List<Setting> settings = userService.getSettings();
        return ResponseEntity.ok(settings);
    }

    @PostMapping("/setting")
    public ResponseEntity<Setting> updateSetting(@Valid @RequestBody Setting setting) {
        log.debug("Settings to update: " + setting);
        Setting updatedSetting = userService.updateSetting(setting);
        return ResponseEntity.ok(updatedSetting);
    }

    @DeleteMapping("/setting/delete")
    public ResponseEntity<Boolean> deleteSetting(@RequestParam Integer settingId) {
        Boolean isSuccess = userService.deleteSetting(settingId);
        return ResponseEntity.ok(isSuccess);
    }
}
