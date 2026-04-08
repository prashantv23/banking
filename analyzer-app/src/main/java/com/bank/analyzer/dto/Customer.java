package com.bank.analyzer.dto;

import java.math.BigDecimal;

public class Customer {
    private Long id;
    private String email;
    private String name;
    private int age;
    private BigDecimal accountBalance;
    private LoanType existingLoanType;
    private boolean existingLoanOpen;
    private LoanType selectedOffer;

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public BigDecimal getAccountBalance() { return accountBalance; }
    public LoanType getExistingLoanType() { return existingLoanType; }
    public boolean isExistingLoanOpen() { return existingLoanOpen; }
    public LoanType getSelectedOffer() { return selectedOffer; }

    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setAccountBalance(BigDecimal accountBalance) { this.accountBalance = accountBalance; }
    public void setExistingLoanType(LoanType existingLoanType) { this.existingLoanType = existingLoanType; }
    public void setExistingLoanOpen(boolean existingLoanOpen) { this.existingLoanOpen = existingLoanOpen; }
    public void setSelectedOffer(LoanType selectedOffer) { this.selectedOffer = selectedOffer; }
}
