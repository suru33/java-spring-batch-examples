package com.suru.springbatch.skipitemsjob.components;

public class CustomSkipException extends Exception {

    public CustomSkipException(String message, int count) {
        super(message + ", attempt count: " + count);
    }

}
