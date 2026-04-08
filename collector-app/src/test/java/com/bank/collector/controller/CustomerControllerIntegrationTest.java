package com.bank.collector.controller;

import com.bank.collector.application.CollectorApplication;
import com.bank.collector.model.Customer;
import com.bank.collector.model.LoanType;
import com.bank.collector.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CollectorApplication.class, properties = {
        "banking.dataset.import-on-startup=false",
        "spring.datasource.url=jdbc:h2:mem:collector_test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
})
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void shouldFetchCustomerAndAcceptOffer() throws Exception {
        customerRepository.save(new Customer("integration@example.com", "Integration", 31,
                new BigDecimal("50000"), LoanType.AUTO, false, null));

        mockMvc.perform(get("/collector/customer").param("email", "integration@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("integration@example.com"));

        mockMvc.perform(post("/collector/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"integration@example.com\",\"loanType\":\"PERSONAL\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Offer accepted")));
    }
}
