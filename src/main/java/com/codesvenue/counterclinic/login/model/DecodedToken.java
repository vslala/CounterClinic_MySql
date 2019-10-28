package com.codesvenue.counterclinic.login.model;

import lombok.Data;

@Data
public class DecodedToken {

    private String userId;
    private String[] roles;

    public DecodedToken userId(String userId) {
        this.userId = userId;
        return this;
    }

    public DecodedToken roles(String[] roles) {
        this.roles = roles;
        return this;
    }
}
