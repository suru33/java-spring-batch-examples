package com.suru.springbatch.itemreaderexample.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ItemReaderJob {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public SimpleItemReader simpleItemReader() {
        return new SimpleItemReader(Arrays.asList("One", "Two", "Three", "Four", "Five"));
    }

    @Bean
    public Step itemReaderStep() {
        return stepBuilderFactory.get("item-reader-step")
                .<String, String>chunk(2)
                .reader(simpleItemReader())
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        for (String item : items) {
                            System.out.println("item write: " + item);
                        }
                    }
                }).build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(itemReaderStep())
                .build();
    }

}
