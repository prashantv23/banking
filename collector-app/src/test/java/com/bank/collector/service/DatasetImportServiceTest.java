package com.bank.collector.service;

import com.bank.collector.dto.DatasetImportResponse;
import com.bank.collector.model.BankingRecord;
import com.bank.collector.model.Customer;
import com.bank.collector.model.LoanType;
import com.bank.collector.repository.BankingRecordRepository;
import com.bank.collector.repository.CustomerRepository;
import com.bank.collector.support.client.GitHubDatasetClient;
import com.bank.collector.support.config.DatasetImportProperties;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class DatasetImportServiceTest {

    @Test
    void shouldImportRecordsAndDeriveCustomersFromDataset() {
        GitHubDatasetClient client = mock(GitHubDatasetClient.class);
        BankingRecordRepository bankingRecordRepository = mock(BankingRecordRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        DatasetImportProperties properties = new DatasetImportProperties();
        properties.setMetadataUrl("https://api.github.com/example");

        when(bankingRecordRepository.count()).thenReturn(0L);
        when(customerRepository.count()).thenReturn(0L);
        when(client.fetchDatasetCsv(properties.getMetadataUrl())).thenReturn("""
                Customer ID,First Name,Last Name,Age,Gender,Address,City,Contact Number,Email,Account Type,Account Balance,Date Of Account Opening,Last Transaction Date,TransactionID,Transaction Date,Transaction Type,Transaction Amount,Account Balance After Transaction,Branch ID,Loan ID,Loan Amount,Loan Type,Interest Rate,Loan Term,Approval/Rejection Date,Loan Status,CardID,Card Type,Credit Limit,Credit Card Balance,Minimum Payment Due,Payment Due Date,Last Credit Card Payment Date,Rewards Points,Feedback ID,Feedback Date,Feedback Type,Resolution Status,Resolution Date,Anomaly
                1,Joshua,Hall,45,Male,Address_1,Fort Worth,19458794854,joshua.hall@kag.com,Current,1313.38,5/26/2006,4/23/2023,1,12/7/2023,Withdrawal,1457.61,2770.99,43,1,32200.06,Mortgage,2.64,36,5/11/2021,Rejected,1,AMEX,1737.88,4524.32,226.22,11/26/2023,3/20/2023,8142,1,10/6/2023,Suggestion,Resolved,1/22/2023,1
                2,Mark,Taylor,47,Female,Address_2,Louisville,19458794855,mark.taylor@kag.com,Current,5988.46,3/2/2006,1/27/2023,2,4/27/2023,Deposit,1660.99,7649.45,63,2,47743.52,Auto,2.48,36,7/30/2020,Approved,2,MasterCard,1799.36,856.7,42.84,11/5/2023,6/16/2023,4306,2,4/7/2023,Complaint,Resolved,8/27/2023,1
                """);

        DatasetImportService service = new DatasetImportService(client, properties, bankingRecordRepository, customerRepository);

        DatasetImportResponse response = service.importDataset();

        assertEquals(2, response.importedRecords());
        assertEquals(properties.getMetadataUrl(), response.source());
        assertFalse(response.skipped());

        ArgumentCaptor<List<BankingRecord>> recordCaptor = ArgumentCaptor.forClass(List.class);
        verify(bankingRecordRepository).saveAll(recordCaptor.capture());
        assertEquals(2, recordCaptor.getValue().size());
        assertEquals("1", recordCaptor.getValue().get(0).getTransactionId());
        assertEquals(new BigDecimal("2770.99"), recordCaptor.getValue().get(0).getAccountBalance());

        ArgumentCaptor<List<Customer>> customerCaptor = ArgumentCaptor.forClass(List.class);
        verify(customerRepository).saveAll(customerCaptor.capture());
        assertEquals(2, customerCaptor.getValue().size());
        assertEquals("joshua.hall@kag.com", customerCaptor.getValue().get(0).getEmail());
        assertEquals(LoanType.MORTGAGE, customerCaptor.getValue().get(0).getExistingLoanType());
    }

    @Test
    void shouldSkipImportWhenRecordsAlreadyExist() {
        GitHubDatasetClient client = mock(GitHubDatasetClient.class);
        BankingRecordRepository bankingRecordRepository = mock(BankingRecordRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        DatasetImportProperties properties = new DatasetImportProperties();
        properties.setMetadataUrl("https://api.github.com/example");

        when(bankingRecordRepository.count()).thenReturn(5L);

        DatasetImportService service = new DatasetImportService(client, properties, bankingRecordRepository, customerRepository);

        DatasetImportResponse response = service.importDataset();

        assertTrue(response.skipped());
        assertEquals(0, response.importedRecords());
        verifyNoInteractions(client);
        verify(bankingRecordRepository, never()).saveAll(anyList());
        verify(customerRepository, never()).saveAll(anyList());
    }
}
