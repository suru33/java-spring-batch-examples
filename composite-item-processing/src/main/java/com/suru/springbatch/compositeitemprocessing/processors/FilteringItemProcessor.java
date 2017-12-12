package com.suru.springbatch.compositeitemprocessing.processors;

import com.suru.springbatch.compositeitemprocessing.domain.Customer;
import org.springframework.batch.item.ItemProcessor;

public class FilteringItemProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer item) throws Exception {
        // filter all customers with even ID
        return item.getId() % 2 == 0 ? null : item;
    }
}
