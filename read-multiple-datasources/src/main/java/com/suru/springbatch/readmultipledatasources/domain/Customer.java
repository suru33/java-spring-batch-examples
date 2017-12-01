package com.suru.springbatch.readmultipledatasources.domain;

import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

/**
 * ResourceAware is spring automatically gives the information
 * about which resource you are using when using multiple resources
 */
public class Customer implements ResourceAware {
    private String id;
    private String firstName;
    private String lastName;
    private String random;
    // optional
    private Resource resource;

    public Customer(String id, String firstName, String lastName, String random) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.random = random;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", random='" + random + '\'' +
                ", resource=" + resource +
                '}';
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
