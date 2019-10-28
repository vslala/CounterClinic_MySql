package com.codesvenue.counterclinic.login.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.codesvenue.counterclinic.login.exception.LoginException;
import com.codesvenue.counterclinic.login.model.DecodedToken;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class Auth0JwtToken implements JwtToken {

    private final String secretKey;
    private final String issuer;

    public Auth0JwtToken(@Value("${jwt.token.secretKey}") String secretKey, @Value("${jwt.token.iss}") String issuer) {
        this.secretKey = secretKey;
        this.issuer = issuer;
    }

    public String generateToken(String userId, String[] roles) {
        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("userId", userId)
                    .withArrayClaim("roles", roles)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            log.error("Error occurred while generating jwt token", exception);
            throw new LoginException("Error occurred while generating JWT token.");
        }
        return token;
    }

    public DecodedToken getDecodedToken(String token) {
        DecodedJWT jwt = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            log.error("Error occurred while decoding jwt token", exception);
            throw  new LoginException("Error occurred while decoding JWT token.");
        }
        return prepareDecodedToken(jwt);
    }

    private DecodedToken prepareDecodedToken(DecodedJWT jwt) {
        return new DecodedToken()
                .userId(jwt.getClaim("userId").asString())
                .roles(jwt.getClaim("roles").asArray(String.class));
    }
}
