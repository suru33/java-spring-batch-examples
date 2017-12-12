package com.suru.springbatch.restartjob.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestartJobDemoJobConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    @StepScope // creates new object for each step
    public Tasklet restartDemoTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
                if (executionContext.containsKey("pass")) {
                    System.out.println("Done! with this step. Proceed to next!");
                    return RepeatStatus.FINISHED;
                } else {
                    System.out.println("I am stopping this step execution");
                    executionContext.put("pass", true);
                    throw new RuntimeException("!! this exception is for demo !!");
                }
            }
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("restart-job-step-1")
                .tasklet(restartDemoTasklet())
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("restart-job-step-2")
                .tasklet(restartDemoTasklet())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("restart-demo-test-job")
                .start(step1())
                .next(step2())
                .build();
    }
}
