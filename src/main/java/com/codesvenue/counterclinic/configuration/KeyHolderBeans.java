package com.codesvenue.counterclinic.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

@Component
public class KeyHolderBeans {

//    @Profile("!default")
    @Bean
    public GeneratedKeyHolder defaultKeyHolder() {
        return new GeneratedKeyHolder();
    }
}
