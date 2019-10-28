package com.codesvenue.counterclinic.login.model;

import com.codesvenue.counterclinic.user.model.User;
import lombok.Data;

@Data
public class LoginResponse {
    private User user;
    private String accessToken;

    public static LoginResponse newInstance() {
        return new LoginResponse();
    }

    public LoginResponse user(User user) {
        this.user = user;
        return this;
    }

    public LoginResponse accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
