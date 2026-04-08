package com.bank.analyzer.benchmark;

import com.bank.analyzer.dto.Customer;
import com.bank.analyzer.service.LoanEligibilityService;
import org.openjdk.jmh.annotations.*;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class LoanEligibilityBenchmark {

    private LoanEligibilityService service;
    private Customer customer;

    @Setup
    public void setup() {
        service = new LoanEligibilityService();
        customer = new Customer();
        customer.setAge(35);
        customer.setAccountBalance(new BigDecimal("100000"));
        customer.setExistingLoanOpen(false);
    }

    @Benchmark
    public Object benchmarkEligibilityCalculation() {
        return service.analyze(customer);
    }
}
