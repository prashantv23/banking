package com.bank.collector.service;

import com.bank.collector.dto.DatasetImportResponse;
import com.bank.collector.model.BankingRecord;
import com.bank.collector.model.Customer;
import com.bank.collector.model.LoanType;
import com.bank.collector.repository.BankingRecordRepository;
import com.bank.collector.repository.CustomerRepository;
import com.bank.collector.support.client.GitHubDatasetClient;
import com.bank.collector.support.config.DatasetImportProperties;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DatasetImportService {

    private static final DateTimeFormatter DATASET_DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");

    private static final String CUSTOMER_ID = "Customer ID";
    private static final String TRANSACTION_ID = "TransactionID";
    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String EMAIL = "Email";
    private static final String TRANSACTION_DATE = "Transaction Date";
    private static final String TRANSACTION_TYPE = "Transaction Type";
    private static final String TRANSACTION_AMOUNT = "Transaction Amount";
    private static final String ACCOUNT_BALANCE_AFTER_TRANSACTION = "Account Balance After Transaction";
    private static final String CUSTOMER_AGE = "Age";
    private static final String CUSTOMER_GENDER = "Gender";
    private static final String ACCOUNT_TYPE = "Account Type";
    private static final String BRANCH_ID = "Branch ID";
    private static final String ACCOUNT_OPENING_DATE = "Date Of Account Opening";
    private static final String LOAN_TYPE = "Loan Type";
    private static final String LOAN_STATUS = "Loan Status";

    private final GitHubDatasetClient gitHubDatasetClient;
    private final DatasetImportProperties properties;
    private final BankingRecordRepository bankingRecordRepository;
    private final CustomerRepository customerRepository;

    public DatasetImportService(GitHubDatasetClient gitHubDatasetClient,
                                DatasetImportProperties properties,
                                BankingRecordRepository bankingRecordRepository,
                                CustomerRepository customerRepository) {
        this.gitHubDatasetClient = gitHubDatasetClient;
        this.properties = properties;
        this.bankingRecordRepository = bankingRecordRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public DatasetImportResponse importDataset() {
        if (bankingRecordRepository.count() > 0) {
            return new DatasetImportResponse(0, true, properties.getMetadataUrl());
        }

        String csvBody = gitHubDatasetClient.fetchDatasetCsv(properties.getMetadataUrl());
        List<BankingRecord> records = parseCsv(csvBody);
        bankingRecordRepository.saveAll(records);
        saveCustomers(records);
        return new DatasetImportResponse(records.size(), false, properties.getMetadataUrl());
    }

    @Transactional(readOnly = true)
    public long countImportedRecords() {
        return bankingRecordRepository.count();
    }

    private List<BankingRecord> parseCsv(String csvBody) {
        try (CSVParser parser = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .build()
                .parse(new StringReader(csvBody))) {
            List<BankingRecord> records = new ArrayList<>();
            for (CSVRecord csvRecord : parser) {
                records.add(new BankingRecord(
                        csvRecord.get(CUSTOMER_ID),
                        csvRecord.get(TRANSACTION_ID),
                        csvRecord.get(FIRST_NAME),
                        csvRecord.get(LAST_NAME),
                        csvRecord.get(EMAIL),
                        parseDate(csvRecord.get(TRANSACTION_DATE)),
                        csvRecord.get(TRANSACTION_TYPE),
                        new BigDecimal(csvRecord.get(TRANSACTION_AMOUNT)),
                        new BigDecimal(csvRecord.get(ACCOUNT_BALANCE_AFTER_TRANSACTION)),
                        Integer.parseInt(csvRecord.get(CUSTOMER_AGE)),
                        csvRecord.get(CUSTOMER_GENDER),
                        csvRecord.get(ACCOUNT_TYPE),
                        csvRecord.get(BRANCH_ID),
                        parseDate(csvRecord.get(ACCOUNT_OPENING_DATE)),
                        null,
                        csvRecord.get(LOAN_TYPE),
                        csvRecord.get(LOAN_STATUS)
                ));
            }
            return records;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV dataset", e);
        }
    }

    private void saveCustomers(List<BankingRecord> records) {
        if (customerRepository.count() > 0) {
            return;
        }

        Map<String, BankingRecord> firstRecordByEmail = records.stream()
                .collect(Collectors.toMap(BankingRecord::getEmail, Function.identity(), (left, right) -> left));

        List<Customer> customers = firstRecordByEmail.values().stream()
                .map(this::toCustomer)
                .toList();

        customerRepository.saveAll(customers);
    }

    private Customer toCustomer(BankingRecord record) {
        String fullName = (record.getFirstName() + " " + record.getLastName()).trim();
        LoanType mappedLoanType = mapLoanType(record.getLoanType());
        boolean loanOpen = record.getLoanStatus() != null
                && !record.getLoanStatus().equalsIgnoreCase("Closed");

        return new Customer(
                record.getEmail(),
                fullName,
                record.getCustomerAge(),
                record.getAccountBalance(),
                mappedLoanType,
                loanOpen,
                null
        );
    }

    private LoanType mapLoanType(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LoanType.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    private LocalDate parseDate(String value) {
        return LocalDate.parse(value, DATASET_DATE_FORMAT);
    }
}
