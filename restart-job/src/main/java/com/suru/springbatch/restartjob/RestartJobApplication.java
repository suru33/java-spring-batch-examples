package com.suru.springbatch.restartjob;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class RestartJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestartJobApplication.class, args);
    }
}
