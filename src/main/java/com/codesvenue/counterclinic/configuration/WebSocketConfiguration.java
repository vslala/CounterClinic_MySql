package com.codesvenue.counterclinic.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint(EndPoint.WEBSOCKET.getEndPointUrl()).withSockJS();
        registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker(EndPoint.DOCTOR_ACTION_CALL_NEXT_PATIENT.getTopic());
        config.enableSimpleBroker("/topic");
//        config.setApplicationDestinationPrefixes(EndPoint.WEBSOCKET.getTopic());
        config.setApplicationDestinationPrefixes("/app");
    }
}
