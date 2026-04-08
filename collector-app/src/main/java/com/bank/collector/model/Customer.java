package com.bank.collector.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;
    private int age;

    @Column(nullable = false)
    private BigDecimal accountBalance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private LoanType existingLoanType;

    private boolean existingLoanOpen;

    @Enumerated(EnumType.STRING)
    private LoanType selectedOffer;

    public Customer() {
    }

    public Customer(String email, String name, int age, BigDecimal accountBalance,
                    LoanType existingLoanType, boolean existingLoanOpen, LoanType selectedOffer) {
        this.email = email;
        this.name = name;
        this.age = age;
        this.accountBalance = accountBalance;
        this.existingLoanType = existingLoanType;
        this.existingLoanOpen = existingLoanOpen;
        this.selectedOffer = selectedOffer;
    }

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
