package com.suru.springbatch.flowapplication.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowLastConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step startingStep() {
        return stepBuilderFactory.get("FlowLast Step")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>>>>> FlowLast Out");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Job fistJobLastFlow(@Qualifier("flow-bean") Flow flow) {
        return jobBuilderFactory.get("FlowLast Job")
                .start(startingStep())
                .on("COMPLETED").to(flow)
                .end()
                .build();
    }

}
