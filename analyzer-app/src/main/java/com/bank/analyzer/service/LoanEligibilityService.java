package com.bank.analyzer.service;

import com.bank.analyzer.dto.Customer;
import com.bank.analyzer.dto.LoanOffer;
import com.bank.analyzer.dto.LoanType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanEligibilityService {

    public List<LoanOffer> analyze(Customer customer) {
        List<LoanOffer> offers = new ArrayList<>();

        if (customer.getSelectedOffer() != null) {
            return offers;
        }

        if (customer.getAge() < 18 || customer.getAge() > 65) {
            return offers;
        }

        addOffer(offers, customer, LoanType.MORTGAGE, new BigDecimal("500000"), 8.5, 240);
        addOffer(offers, customer, LoanType.AUTO, new BigDecimal("200000"), 9.5, 60);
        addOffer(offers, customer, LoanType.PERSONAL, new BigDecimal("100000"), 12.0, 36);

        return offers;
    }

    private void addOffer(List<LoanOffer> offers, Customer customer, LoanType type,
                          BigDecimal amount, double rate, int tenureMonths) {
        if (customer.isExistingLoanOpen() && type == customer.getExistingLoanType()) {
            return;
        }

        BigDecimal minBalance = amount.divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP);
        if (customer.getAccountBalance() != null && customer.getAccountBalance().compareTo(minBalance) >= 0) {
            offers.add(new LoanOffer(type, amount, rate, tenureMonths,
                    "Eligible based on age, balance and existing-loan rules"));
        }
    }
}
