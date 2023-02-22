package com.orphanet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RareDiseasesDatabaseApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(RareDiseasesDatabaseApplication.class, args);
	}

}
