package com.suru.springbatch.retryjob.components;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class RetryItemWriter implements ItemWriter<String> {

    private int retryCount = 0;

    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String i : items) {
            System.out.println("start writing item: " + i);
            if (i.equalsIgnoreCase("-24")) {
                retryCount++;
                if (retryCount >= 5) {
                    System.out.println("success writing item: " + i);
                } else {
                    String exMessage = "**error writing item: " + i + "**";
                    System.out.println(exMessage);
                    throw new CustomRetryException(exMessage, retryCount);
                }
            } else {
                System.out.println("writing item: " + i);
            }

        }
    }
}
