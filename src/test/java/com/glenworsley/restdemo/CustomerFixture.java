package com.glenworsley.restdemo;

public class CustomerFixture {

    public static Customer createCustomer(Long id, String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName);
        customer.setId(id);
        return customer;
    }
}
