package com.bank.collector.controller;

import com.bank.collector.dto.AcceptOfferRequest;
import com.bank.collector.model.Customer;
import com.bank.collector.service.CustomerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collector")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customer")
    public Customer customer(@RequestParam String email) {
        return customerService.getCustomer(email);
    }

    @PostMapping("/accept")
    public String accept(@RequestBody AcceptOfferRequest request) {
        return customerService.acceptOffer(request);
    }
}
