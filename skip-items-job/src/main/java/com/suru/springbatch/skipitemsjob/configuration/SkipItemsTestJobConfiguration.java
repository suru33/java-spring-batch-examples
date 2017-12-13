package com.suru.springbatch.skipitemsjob.configuration;

import com.suru.springbatch.skipitemsjob.components.CustomSkipException;
import com.suru.springbatch.skipitemsjob.components.SkipItemProcessor;
import com.suru.springbatch.skipitemsjob.components.SkipItemWriter;
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
public class SkipItemsTestJobConfiguration {

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
        return new SkipItemWriter();
    }

    @Bean
    public ItemProcessor<String, String> stringItemProcessor() {
        return new SkipItemProcessor();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("skip-items-test-step-1")
                .<String, String>chunk(5)
                .reader(stringItemReader())
                .processor(stringItemProcessor())
                .writer(stringItemWriter())
                .faultTolerant()
                .skip(CustomSkipException.class)
                .skipLimit(15)
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("skip-items-test-job")
                .start(step1())
                .build();
    }
}
