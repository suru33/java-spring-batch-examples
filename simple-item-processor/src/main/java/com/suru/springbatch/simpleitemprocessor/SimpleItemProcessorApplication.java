package com.suru.springbatch.simpleitemprocessor;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SimpleItemProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleItemProcessorApplication.class, args);
	}
}
