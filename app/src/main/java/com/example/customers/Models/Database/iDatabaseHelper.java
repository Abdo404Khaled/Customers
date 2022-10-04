package com.example.customers.Models.Database;

import com.example.customers.Models.Customer;

import java.util.List;

interface iDatabaseHelper {

    public boolean addNewCustomer(Customer customer);

    public List<Customer> getAllCustomers();

    public boolean deleteCustomer(Customer customer);

}
