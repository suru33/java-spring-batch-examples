package com.suru.springbatch.listenersapplication.listeners;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(">> Before Job: " + jobExecution);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(">> After Job: " + jobExecution);
    }
}
