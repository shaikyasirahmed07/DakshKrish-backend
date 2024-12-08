package com.sdk.kheeti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;




@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class KheetiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KheetiApplication.class, args);
	}

}
