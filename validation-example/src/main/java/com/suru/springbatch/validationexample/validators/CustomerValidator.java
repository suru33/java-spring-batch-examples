package com.suru.springbatch.validationexample.validators;

import com.suru.springbatch.validationexample.domain.Customer;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

public class CustomerValidator implements Validator<Customer> {

    @Override
    public void validate(Customer value) throws ValidationException {
        if (value.getFirstName().toLowerCase().charAt(0) == 'a') {
            throw new ValidationException("firstName start with 'a' not valid: " + value);
        }
    }
}
