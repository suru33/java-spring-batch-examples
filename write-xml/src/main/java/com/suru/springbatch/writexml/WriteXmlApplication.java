package com.suru.springbatch.writexml;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class WriteXmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(WriteXmlApplication.class, args);
	}
}
