package com.suru.springbatch.retryjob.configuration;

import com.suru.springbatch.retryjob.components.CustomRetryException;
import com.suru.springbatch.retryjob.components.RetryItemProcessor;
import com.suru.springbatch.retryjob.components.RetryItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RetryTestJobConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public ItemReader<String> stringItemReader() {
        List<String> strings = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            strings.add(Integer.toString(i));
        }
        ListItemReader<String> itemReader = new ListItemReader<>(strings);
        return itemReader;
    }

    @Bean
    public ItemWriter<String> stringItemWriter() {
        return new RetryItemWriter();
    }

    @Bean
    public ItemProcessor<String, String> stringItemProcessor() {
        return new RetryItemProcessor();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("retry-test-step-1")
                .<String, String>chunk(5)
                .reader(stringItemReader())
                .processor(stringItemProcessor())
                .writer(stringItemWriter())
                .faultTolerant()
                .retry(CustomRetryException.class) // retry for this particular exception
                .retryLimit(10) // retry limit
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("retry-test-job-x")
                .start(step1())
                .build();
    }
}
