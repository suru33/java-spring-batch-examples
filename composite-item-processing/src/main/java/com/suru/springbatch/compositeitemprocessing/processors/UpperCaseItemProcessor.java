package com.suru.springbatch.compositeitemprocessing.processors;

import com.suru.springbatch.compositeitemprocessing.domain.CustomerName;
import org.springframework.batch.item.ItemProcessor;

public class UpperCaseItemProcessor implements ItemProcessor<CustomerName, CustomerName> {
    @Override
    public CustomerName process(CustomerName item) throws Exception {
        return new CustomerName(item.getFirstName().toUpperCase(), item.getLastName().toUpperCase());
    }
}
