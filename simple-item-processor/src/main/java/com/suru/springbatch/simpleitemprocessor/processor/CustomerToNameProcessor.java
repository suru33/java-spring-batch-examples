package com.suru.springbatch.simpleitemprocessor.processor;

import com.suru.springbatch.simpleitemprocessor.domain.Customer;
import com.suru.springbatch.simpleitemprocessor.domain.Name;
import org.springframework.batch.item.ItemProcessor;

public class CustomerToNameProcessor implements ItemProcessor<Customer, Name> {
    @Override
    public Name process(Customer item) throws Exception {
        // some processing
        return new Name(item.getFirstName().toUpperCase(), item.getLastName().toUpperCase());
    }
}
