package com.nyakako.simplesave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SimplesaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplesaveApplication.class, args);
	}

}
