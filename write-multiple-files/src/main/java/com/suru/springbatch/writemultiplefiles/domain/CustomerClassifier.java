package com.suru.springbatch.writemultiplefiles.domain;

import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

public class CustomerClassifier implements Classifier<Customer, ItemWriter<? super Customer>> {

    private ItemWriter<Customer> xmlWriter;
    private ItemWriter<Customer> jsonWriter;

    public CustomerClassifier(ItemWriter<Customer> xmlWriter, ItemWriter<Customer> jsonWriter) {
        this.xmlWriter = xmlWriter;
        this.jsonWriter = jsonWriter;
    }

    @Override
    public ItemWriter<? super Customer> classify(Customer classifiable) {
        return classifiable.getId() % 2 == 0 ? xmlWriter : jsonWriter;
    }
}
