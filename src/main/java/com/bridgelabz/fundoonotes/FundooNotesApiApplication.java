package com.bridgelabz.fundoonotes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import lombok.extern.slf4j.Slf4j;


@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
@Slf4j
public class FundooNotesApiApplication {
private static final Logger log=LoggerFactory.getLogger(FundooNotesApiApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(FundooNotesApiApplication.class, args);
		log.info("now main application gets started");
	}

}
