package com.webbook.webbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
@RestController
public class WebbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebbookApplication.class, args);
	}

	@GetMapping("/test")
	public String test() {
		return "TEST Endpoint";
	}
}
