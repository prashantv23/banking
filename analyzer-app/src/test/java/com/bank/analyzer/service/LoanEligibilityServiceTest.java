package com.bank.analyzer.service;

import com.bank.analyzer.dto.Customer;
import com.bank.analyzer.dto.LoanType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanEligibilityServiceTest {

    private final LoanEligibilityService service = new LoanEligibilityService();

    @Test
    void shouldReturnNoOffersForUnderageCustomer() {
        Customer customer = new Customer();
        customer.setAge(17);
        customer.setAccountBalance(new BigDecimal("100000"));
        customer.setExistingLoanOpen(false);

        assertTrue(service.analyze(customer).isEmpty());
    }

    @Test
    void shouldNotOfferExistingLoanTypeWhenOpen() {
        Customer customer = new Customer();
        customer.setAge(28);
        customer.setAccountBalance(new BigDecimal("60000"));
        customer.setExistingLoanType(LoanType.PERSONAL);
        customer.setExistingLoanOpen(true);

        assertTrue(service.analyze(customer).stream().noneMatch(o -> o.loanType() == LoanType.PERSONAL));
    }

    @Test
    void shouldOfferPersonalLoanWhenBalanceQualifies() {
        Customer customer = new Customer();
        customer.setAge(28);
        customer.setAccountBalance(new BigDecimal("10000"));
        customer.setExistingLoanOpen(false);

        assertTrue(service.analyze(customer).stream().anyMatch(o -> o.loanType() == LoanType.PERSONAL));
    }
}
