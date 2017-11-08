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

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step flowStep1() {
        return stepBuilderFactory.get("FlowBuilder-Step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>>> FlowBuilder-Step1-Out");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step flowStep2() {
        return stepBuilderFactory.get("FlowBuilder-Step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>>>>> FlowBuilder-Step2-Out");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean(name = "flow-bean")
    public Flow buildFlow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("Flow-Builder");
        flowBuilder.start(flowStep1())
                .next(flowStep2())
                .end();
        return flowBuilder.build();
    }

}
