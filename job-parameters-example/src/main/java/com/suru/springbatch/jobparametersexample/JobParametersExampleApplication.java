package com.suru.springbatch.jobparametersexample;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableBatchProcessing
public class JobParametersExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobParametersExampleApplication.class, args);
	}
}
