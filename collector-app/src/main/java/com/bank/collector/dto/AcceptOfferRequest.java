package com.bank.collector.dto;

import com.bank.collector.model.LoanType;

public class AcceptOfferRequest {
    private String email;
    private LoanType loanType;

    public String getEmail() { return email; }
    public LoanType getLoanType() { return loanType; }

    public void setEmail(String email) { this.email = email; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }
}
