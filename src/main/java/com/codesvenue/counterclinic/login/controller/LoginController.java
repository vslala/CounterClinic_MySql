package com.codesvenue.counterclinic.login.controller;

import com.codesvenue.counterclinic.login.model.LoginCredentials;
import com.codesvenue.counterclinic.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Log4j
@RestController
@Api("Login Controller")
@RequestMapping("/login")
public class LoginController {

    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Validates login credentials of a user", response = ResponseEntity.class)
    @PostMapping
    public ResponseEntity loginUser(@Valid @RequestBody LoginCredentials loginCredentials) {
        log.debug("Request Received: " + loginCredentials);
        return ResponseEntity.ok(userService.loginUser(loginCredentials));
    }
}
