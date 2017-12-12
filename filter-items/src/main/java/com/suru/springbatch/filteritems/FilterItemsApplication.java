package com.suru.springbatch.filteritems;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class FilterItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilterItemsApplication.class, args);
	}
}
