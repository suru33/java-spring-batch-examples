package com.suru.springbatch.xmlreader;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableBatchProcessing
public class XmlReaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(XmlReaderApplication.class, args);
    }
}
