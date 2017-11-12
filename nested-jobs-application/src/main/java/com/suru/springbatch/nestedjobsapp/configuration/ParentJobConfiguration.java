package com.suru.springbatch.nestedjobsapp.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ParentJobConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    @Qualifier("childJobBean")
    private Job childJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Step parentStep() {
        return stepBuilderFactory.get("parent-job-step")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>> parent step out");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Job parentJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Step childStep = new JobStepBuilder(new StepBuilder("childJobStepInParent"))
                .job(childJob)
                .launcher(jobLauncher)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();

        return jobBuilderFactory.get("parent-job")
                .start(parentStep())
                .next(childStep)
                .build();
    }

}
