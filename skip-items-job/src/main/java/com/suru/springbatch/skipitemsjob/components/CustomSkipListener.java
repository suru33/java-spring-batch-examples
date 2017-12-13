package com.suru.springbatch.skipitemsjob.components;

import org.springframework.batch.core.SkipListener;

public class CustomSkipListener implements SkipListener<String, String> {
    @Override
    public void onSkipInRead(Throwable t) {
        System.out.println("read error: " + t);
    }

    @Override
    public void onSkipInWrite(String item, Throwable t) {
        System.out.println("write error: " + item + ", cause: " + t.getMessage());
    }

    @Override
    public void onSkipInProcess(String item, Throwable t) {
        System.out.println("process error: " + item + ", cause: " + t.getMessage());
    }
}
