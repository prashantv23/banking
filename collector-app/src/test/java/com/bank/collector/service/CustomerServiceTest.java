package com.bank.collector.service;

import com.bank.collector.dto.AcceptOfferRequest;
import com.bank.collector.model.Customer;
import com.bank.collector.model.LoanType;
import com.bank.collector.repository.CustomerRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Test
    void shouldUpdateCustomerWhenOfferAccepted() {
        CustomerRepository repository = mock(CustomerRepository.class);
        Customer customer = new Customer("a@b.com", "A", 30, new BigDecimal("25000"), null, false, null);
        when(repository.findByEmail("a@b.com")).thenReturn(Optional.of(customer));

        CustomerService service = new CustomerService(repository);
        AcceptOfferRequest request = new AcceptOfferRequest();
        request.setEmail("a@b.com");
        request.setLoanType(LoanType.PERSONAL);

        String result = service.acceptOffer(request);

        assertEquals("Offer accepted for a@b.com", result);
        assertEquals(LoanType.PERSONAL, customer.getSelectedOffer());
        assertEquals(new BigDecimal("20000"), customer.getAccountBalance());
        verify(repository).save(customer);
    }
}
