package com.suru.springbatch.compositeitemprocessing.processors;

import com.suru.springbatch.compositeitemprocessing.domain.Customer;
import com.suru.springbatch.compositeitemprocessing.domain.CustomerName;
import org.springframework.batch.item.ItemProcessor;

public class CustomerNameItemProcessor implements ItemProcessor<Customer, CustomerName> {
    @Override
    public CustomerName process(Customer item) throws Exception {
        return new CustomerName(item.getFirstName(), item.getLastName());
    }
}
