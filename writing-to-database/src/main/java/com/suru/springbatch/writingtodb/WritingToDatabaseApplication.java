package com.suru.springbatch.writingtodb;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class WritingToDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(WritingToDatabaseApplication.class, args);
	}
}
