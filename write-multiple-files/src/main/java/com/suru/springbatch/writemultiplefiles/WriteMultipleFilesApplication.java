package com.suru.springbatch.writemultiplefiles;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class WriteMultipleFilesApplication {

	public static void main(String[] args) {
		SpringApplication.run(WriteMultipleFilesApplication.class, args);
	}
}
