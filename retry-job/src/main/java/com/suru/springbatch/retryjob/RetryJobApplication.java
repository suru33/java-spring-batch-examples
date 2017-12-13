package com.suru.springbatch.retryjob;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class RetryJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetryJobApplication.class, args);
	}
}
