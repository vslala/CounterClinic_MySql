package com.codesvenue.counterclinic.login.model;

import com.codesvenue.counterclinic.user.model.User;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginCredentials {
    @NotNull
    private String username;
    @NotNull
    private String password;

    public static LoginCredentials newInstance() {
        return new LoginCredentials();
    }

    public LoginCredentials username(String username) {
        this.username = username;
        return this;
    }

    public LoginCredentials password(String password) {
        this.password = password;
        return this;
    }
}
