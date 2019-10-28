package com.codesvenue.counterclinic.login.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginCredentials {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
