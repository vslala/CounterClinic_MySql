package com.codesvenue.counterclinic.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebSocketController {

    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/message")
    public void sendMessage(@RequestParam("msg") String msg) {
        simpMessagingTemplate.convertAndSend("/topic/doctor-action", msg);
    }

    @MessageMapping("/appointment-status")
    @SendTo("/topic/appointment-status")
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
