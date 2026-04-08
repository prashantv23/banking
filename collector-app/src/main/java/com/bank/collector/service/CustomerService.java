package com.bank.collector.service;

import com.bank.collector.dto.AcceptOfferRequest;
import com.bank.collector.model.Customer;
import com.bank.collector.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomer(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + email));
    }

    public String acceptOffer(AcceptOfferRequest request) {
        Customer customer = getCustomer(request.getEmail());
        customer.setSelectedOffer(request.getLoanType());
        customer.setExistingLoanType(request.getLoanType());
        customer.setExistingLoanOpen(true);
        customer.setAccountBalance(customer.getAccountBalance().subtract(new BigDecimal("5000")));
        customerRepository.save(customer);
        return "Offer accepted for " + customer.getEmail();
    }
}
