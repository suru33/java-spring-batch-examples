package com.suru.springbatch.customitemreader.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SimpleJobConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemWriter<String> stringItemWriter() {
        return items -> {
            for (String i : items) {
                System.out.println("item: " + i);
            }
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
        };
    }

    @Bean
    @StepScope
    public MyItemStreamReader myItemReader() {
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            items.add(String.valueOf(i));
        }
        return new MyItemStreamReader(items);
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step-1")
                .<String, String>chunk(10)
                .reader(myItemReader())
                .writer(stringItemWriter())
                .stream(myItemReader())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("custom-reader-job")
                .start(step1())
                .build();
    }

}