package com.suru.springbatch.flowapplication.configuration;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowConfiguration {

    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    public FlowConfiguration(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Step flowStep1() {
        return stepBuilderFactory.get("flowConfigStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>>> flowConfigStep1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step flowStep2() {
        return stepBuilderFactory.get("flowConfigStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>>> flowConfigStep2");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean(name = "my-step-flow")
    public Flow buildFlow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("step-flow-builder");
        flowBuilder.start(flowStep1())
                .next(flowStep2())
                .end();
        return flowBuilder.build();
    }

}
