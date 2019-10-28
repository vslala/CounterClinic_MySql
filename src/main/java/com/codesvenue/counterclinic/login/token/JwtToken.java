package com.codesvenue.counterclinic.login.token;


import com.codesvenue.counterclinic.login.model.DecodedToken;

public interface JwtToken {
    public String generateToken(String userId, String[] roles);

    public DecodedToken getDecodedToken(String token);
}
