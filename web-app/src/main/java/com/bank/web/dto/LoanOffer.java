package com.bank.web.dto;

import java.math.BigDecimal;

public record LoanOffer(
        LoanType loanType,
        BigDecimal amount,
        double interestRate,
        int tenureMonths,
        String reason
) {
}
