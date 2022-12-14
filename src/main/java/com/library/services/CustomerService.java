package com.library.services;

import com.library.entities.Customer;
import com.library.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public Customer save(Customer customer){
        return customerRepository.save(customer);
    }

    public Optional<Customer> findCustomerByEmail(String email){
        return customerRepository.findCustomerByEmail(email);
    }

}
