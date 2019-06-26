package com.codesvenue.counterclinic;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@Log4j
public class CounterClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(CounterClinicApplication.class, args);
		log.debug("Service Started...");
	}

}
