package com.dayanchor.dayanchor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DayanchorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DayanchorApplication.class, args);
	}

}
