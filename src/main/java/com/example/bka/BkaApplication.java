package com.example.bka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BkaApplication.class, args);
	}

}
