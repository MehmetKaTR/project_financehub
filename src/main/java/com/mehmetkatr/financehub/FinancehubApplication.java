package com.mehmetkatr.financehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FinancehubApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancehubApplication.class, args);
	}

}
