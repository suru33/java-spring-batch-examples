package com.suru.springbatch.splitapp.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job job(@Qualifier("flow1") Flow flow1, @Qualifier("flow2") Flow flow2) {
        return jobBuilderFactory.get("job")
                .start(flow1)
                .split(new SimpleAsyncTaskExecutor()).add(flow2)
                .end()
                .build();
    }

}
