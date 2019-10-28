package com.codesvenue.counterclinic.login;

import com.codesvenue.counterclinic.fixtures.AppointmentMother;
import com.codesvenue.counterclinic.login.exception.LoginException;
import com.codesvenue.counterclinic.login.model.DecodedToken;
import com.codesvenue.counterclinic.login.token.Auth0JwtToken;
import com.codesvenue.counterclinic.login.token.JwtToken;
import com.codesvenue.counterclinic.user.model.UserRole;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JwtTokenTest {

    private JwtToken jwtToken;

    @Before
    public void setup() {
        String secretKey = AppointmentMother.SECRET_KEY;
        jwtToken = new Auth0JwtToken(secretKey, "stacc");
    }

    @Test
    public void generateJwtToken() {
        //given
        String userId = "13";
        String[] roles = {UserRole.PATIENT.toString()};
        //when
        String token = jwtToken.generateToken(userId, roles);
        System.out.println("token: "+ token);
        //then
        Assert.assertNotNull(token);
    }

    @Test
    public void decodeToken() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJQQVRJRU5UIl0sImlzcyI6InN0YWNjIiwidXNlcklkIjoiMTMifQ.WRiejdCWJGBVHQcFfVWygDinz0vbMOIAhSFzn4vMqDA";
        DecodedToken decodedToken = jwtToken.getDecodedToken(token);
        Assert.assertNotNull(decodedToken);
        Assert.assertEquals("13", decodedToken.getUserId());
    }

    @Test(expected = LoginException.class)
    public void decodeToken_exception() {
        jwtToken = new Auth0JwtToken("sdfsdf", "stacc");
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJQQVRJRU5UIl0sImlzcyI6InN0YWNjIiwidXNlcklkIjoiMTMifQ.WRiejdCWJGBVHQcFfVWygDinz0vbMOIAhSFzn4vMqDA";
        jwtToken.getDecodedToken(token);
    }
}
