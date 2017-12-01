package com.suru.springbatch.customitemreader;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class CustomItemReaderImplementationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomItemReaderImplementationApplication.class, args);
	}
}
