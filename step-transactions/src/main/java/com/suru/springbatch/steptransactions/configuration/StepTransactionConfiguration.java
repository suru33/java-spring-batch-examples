package com.suru.springbatch.steptransactions.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StepTransactionConfiguration {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    public StepTransactionConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>> Step 1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>> Step 2");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>> Step 3");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>> Step 4");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Job transitionJobSimpleNext() {
        return jobBuilderFactory.get("step-jobs-using-next")
                .start(step1()) // start with step 1
                .next(step2())  // on completion of step 1 execute step 2
                .next(step3())  // then step 3
                .next(step4())  // then step 4 and build
                .build();
    }

    // which is same as above Job
    // but used job flows
    @Bean
    public Job transitionJobNextMethod2() {
        return jobBuilderFactory.get("step-jobs-using-next")
                .start(step1()).on("COMPLETED").to(step2()) // on completion of step 1 go to step 2
                .from(step2()).on("COMPLETED").to(step3())  // on completion of step 2 go to step 3
                .from(step3()).on("COMPLETED").to(step4())  // on completion of step 3 go to step 4
                .end()  // on completion of step 4 end the job
                .build();
    }
}
