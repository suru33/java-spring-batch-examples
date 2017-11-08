package com.suru.springbatch.splitapp.configuration;

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

    @Bean(name = "flow1")
    public Flow flow1() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow-1");
        flowBuilder
                .start(stepBuilderFactory.get("step1").tasklet(new MyTasklet(1, 1)).build())
                .next(stepBuilderFactory.get("step2").tasklet(new MyTasklet(1, 2)).build())
                .end();
        return flowBuilder.build();
    }

    @Bean(name = "flow2")
    public Flow flow2() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow-2");
        flowBuilder
                .start(stepBuilderFactory.get("step3").tasklet(new MyTasklet(2, 3)).build())
                .next(stepBuilderFactory.get("step4").tasklet(new MyTasklet(2, 4)).build())
                .next(stepBuilderFactory.get("step5").tasklet(new MyTasklet(2, 5)).build())
                .end();
        return flowBuilder.build();
    }

    private class MyTasklet implements Tasklet {

        private int flowId;
        private int stepId;

        public MyTasklet(int flowId, int stepId) {
            this.flowId = flowId;
            this.stepId = stepId;
        }

        @Override
        public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            String s = "Flow: " + flowId + ", Step: " + stepId;
            s += ", Name: " + chunkContext.getStepContext().getStepName();
            s += ", Thread: " + Thread.currentThread().getName();
            System.out.println(s);
            return RepeatStatus.FINISHED;
        }
    }

}
