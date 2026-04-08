package com.bank.collector.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "banking_records", uniqueConstraints = {
        @UniqueConstraint(name = "uk_banking_record_row_signature", columnNames = {
                "transaction_id"
        })
})
public class BankingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    @Column(name = "transaction_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal transactionAmount;

    @Column(name = "account_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal accountBalance;

    @Column(name = "customer_age", nullable = false)
    private Integer customerAge;

    @Column(name = "customer_gender", nullable = false)
    private String customerGender;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "branch_id", nullable = false)
    private String branchId;

    @Column(name = "account_opening_date", nullable = false)
    private LocalDate accountOpeningDate;

    @Column(name = "transaction_description", length = 1000)
    private String transactionDescription;

    @Column(name = "loan_type")
    private String loanType;

    @Column(name = "loan_status")
    private String loanStatus;

    public BankingRecord() {
    }

    public BankingRecord(String customerId, String transactionId, String firstName, String lastName, String email,
                         LocalDate transactionDate, String transactionType, BigDecimal transactionAmount,
                         BigDecimal accountBalance, Integer customerAge, String customerGender,
                         String accountType, String branchId, LocalDate accountOpeningDate,
                         String transactionDescription, String loanType, String loanStatus) {
        this.customerId = customerId;
        this.transactionId = transactionId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
        this.accountBalance = accountBalance;
        this.customerAge = customerAge;
        this.customerGender = customerGender;
        this.accountType = accountType;
        this.branchId = branchId;
        this.accountOpeningDate = accountOpeningDate;
        this.transactionDescription = transactionDescription;
        this.loanType = loanType;
        this.loanStatus = loanStatus;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public Integer getCustomerAge() {
        return customerAge;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getBranchId() {
        return branchId;
    }

    public LocalDate getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }

    public String getLoanType() {
        return loanType;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setCustomerAge(Integer customerAge) {
        this.customerAge = customerAge;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setAccountOpeningDate(LocalDate accountOpeningDate) {
        this.accountOpeningDate = accountOpeningDate;
    }

    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }
}
