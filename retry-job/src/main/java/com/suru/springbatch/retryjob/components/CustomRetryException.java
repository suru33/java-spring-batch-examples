package com.suru.springbatch.retryjob.components;

public class CustomRetryException extends Exception {

    public CustomRetryException(String message, int count) {
        super(message + ", attempt count: " + count);
    }

}
