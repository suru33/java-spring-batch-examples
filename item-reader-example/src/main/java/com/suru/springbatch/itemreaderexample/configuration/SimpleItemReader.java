package com.suru.springbatch.itemreaderexample.configuration;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

public class SimpleItemReader implements ItemReader<String> {

    private Iterator<String> iterator;

    public SimpleItemReader(List<String> data) {
        this.iterator = data.iterator();
    }

    @Override
    public String read() throws Exception,
                                UnexpectedInputException,
                                ParseException,
                                NonTransientResourceException {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
}
