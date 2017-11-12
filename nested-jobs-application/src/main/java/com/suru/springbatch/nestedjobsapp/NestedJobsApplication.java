package com.suru.springbatch.nestedjobsapp;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableBatchProcessing
public class NestedJobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NestedJobsApplication.class, args);
    }
}
