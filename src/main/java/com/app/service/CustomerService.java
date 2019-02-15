package com.app.service;

import com.app.exceptions.MyException;
import com.app.model.Customer;
import com.app.repository.impl.CustomerRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class CustomerService
{

    private static CustomerRepositoryImpl customerRepository = new CustomerRepositoryImpl();

    public void insertCustomer(Customer customer)
    {
        if(customer.getEmail() == "" || customer.getSurname() == "" || customer.getAge() <= 0 || customer.getName() == "")
        {
            throw new MyException("CUSTOMER ERROR - CUSTOMER OBJECT IS NULL, PLEASE FILL ALL FIELDS");
        }
        else
        {
            customerRepository.saveOrUpdate(customer);
            System.err.println("\nCustomer has been added successfully !");
        }
    }
    public boolean checkEmail(String email)
    {
        List<Customer> customers = customerRepository.findAll();

        Optional<Customer> customer =
                customers
                        .stream()
                        .filter(c -> c.getEmail().equals(email))
                        .findFirst();

        if (customer.isPresent())
        {
            return true;
        }
        else
        {
            throw new MyException("EMAIL ERROR - THERE IS NO CUSTOMER WITH THAT EMAIL IN DATABASE");
        }
    }
    public Customer getCustomer(String email)
    {
        List<Customer> customers = customerRepository.findAll();

        Optional<Customer> customer =
                customers
                        .stream()
                        .filter(c -> c.getEmail().equals(email))
                        .findFirst();
        if (customer.isPresent())
        {
            return customer.get();
        }
        else
        {
            return null;
        }


    }
    public void deleteCustomer(Customer customer)
    {
        if (customer == null)
        {
            System.err.println("Deleting customer error !");
        }
        else
        {
            customerRepository.delete(customer.getId());
            System.err.println("Customer has been deleted successfully !");
        }
    }
    public void updateCustomer(Customer customer)
    {
        if(customer.getEmail() == "" || customer.getSurname() == "" || customer.getAge() <= 0 || customer.getName() == "")
        {
            throw new MyException("CUSTOMER ERROR - CUSTOMER OBJECT IS NULL, PLEASE FILL ALL FIELDS");
        }
        else
        {
            customerRepository.saveOrUpdate(customer);
            System.err.println("Customer has been updated successfully !");
        }
    }
}
