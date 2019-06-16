package com.codesvenue.counterclinic.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.codesvenue.counterclinic.user.User;
import com.codesvenue.counterclinic.user.UserConstants;
import com.codesvenue.counterclinic.user.UserRepository;
import com.codesvenue.counterclinic.user.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@Log4j
public class GlobalInterceptor implements HandlerInterceptor, WebMvcConfigurer {
    private static final String AUTHORIZATION_HEADER = "authorization";
    private static final String SECRET = "secret";

    private UserRepository userRepository;

    @Autowired
    public GlobalInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader(AUTHORIZATION_HEADER);
        log.debug("Access Token: " + accessToken);

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET.getBytes()))
                .withIssuer("counterclinic").build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);
        Integer userId = decodedJWT.getClaim("userId").asInt();
        User user = userRepository.findDoctorById(userId);

        log.debug("Logged In User: " + user);
        request.setAttribute(UserConstants.LOGGED_IN_USER, user);
        return !Objects.isNull(user);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }
}
