package com.codesvenue.counterclinic.controller;

import com.codesvenue.counterclinic.configuration.EndPoint;
import org.jboss.logging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class WebSocketController {

    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/next-patient")
    @SendTo("/topic/doctor-action")
    public void callNextPatient(String msg) throws InterruptedException {
        System.out.println("Inside Call Next Patient Method");
        Thread.sleep(2000);
        simpMessagingTemplate.convertAndSend("/topic/second-topic", "Message for topic two");
        simpMessagingTemplate.convertAndSend("/topic/doctor-action", "Message for topic one");
    }

    @MessageMapping("/second")
    @SendTo("/topic/second-topic")
    public String callSecondTopic() {
        System.out.println("Inside Second Topic");
        return "Second topic";
    }
}
