package com.suru.springbatch.retryjob.components;

import org.springframework.batch.item.ItemProcessor;

public class RetryItemProcessor implements ItemProcessor<String, String> {

    private int retryCount = 0;

    @Override
    public String process(String item) throws Exception {
        System.out.println("processing: " + item);
        if (item.equalsIgnoreCase("44")) {
            retryCount++;
            if (retryCount >= 5) {
                System.out.println("Processing Success!");
                return String.valueOf(Integer.parseInt(item) * -1);
            } else {
                String exMessage = "**Processing failed: " + item + "**";
                System.out.println(exMessage);
                throw new CustomRetryException(exMessage, this.retryCount);
            }
        }
        return String.valueOf(Integer.parseInt(item) * -1);
    }
}
