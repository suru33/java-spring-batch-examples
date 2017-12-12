package com.suru.springbatch.compositeitemprocessing;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class CompositeItemProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompositeItemProcessingApplication.class, args);
    }
}
