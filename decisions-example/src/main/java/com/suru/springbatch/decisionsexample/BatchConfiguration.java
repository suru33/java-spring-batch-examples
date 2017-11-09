package com.suru.springbatch.decisionsexample;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Step startingStep() {
        return stepBuilderFactory.get("starting-step")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>>>>> starting step out");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step evenStep() {
        return stepBuilderFactory.get("even-step")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>>>>> even step out");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step oddStep() {
        return stepBuilderFactory.get("odd-step")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>>>>> odd step out");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public JobExecutionDecider decider() {
        System.out.println(">>>>>>>>>>> decider() <<<<<<<<<<<<");
        return new OddDecider();
    }

    @Bean
    public Job buildJob() {
        return jobBuilderFactory.get("batch-job")
                .start(startingStep())
                .next(decider())
                .from(decider()).on("ODD").to(oddStep())
                .from(decider()).on("EVEN").to(evenStep())
                .from(oddStep()).on("COMPLETED").to(decider())
                .from(evenStep()).on("COMPLETED").to(decider())
                .from(decider()).on("DONE").end()
                .end()
                .build();
    }

    class OddDecider implements JobExecutionDecider {

        private int count = 0;

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            count++;
            System.out.println(">>>>>>>> count: " + count);
            if (count == 10) {
                return new FlowExecutionStatus("DONE");
            } else if (count % 2 == 0) {
                return new FlowExecutionStatus("EVEN");
            } else {
                return new FlowExecutionStatus("ODD");
            }
        }
    }

}
