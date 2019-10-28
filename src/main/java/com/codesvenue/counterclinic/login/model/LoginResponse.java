package com.codesvenue.counterclinic.login.model;

import com.codesvenue.counterclinic.user.model.User;
import lombok.Data;

@Data
public class LoginResponse {
    private User user;
    private String accessToken;
}
