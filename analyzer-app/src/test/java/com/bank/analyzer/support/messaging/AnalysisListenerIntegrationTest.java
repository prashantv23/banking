package com.bank.analyzer.support.messaging;

import com.bank.analyzer.dto.AnalysisRequest;
import com.bank.analyzer.service.LoanEligibilityService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AnalysisListenerIntegrationTest {

    @Test
    void shouldPublishAnalysisResultAfterFetchingCustomer() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        LoanEligibilityService service = spy(new LoanEligibilityService());

        AnalysisListener listener = new AnalysisListener(service, rabbitTemplate, "http://localhost:8081/collector") {
            @Override
            public void handle(AnalysisRequest request) {
                var customer = new com.bank.analyzer.dto.Customer();
                customer.setEmail(request.email());
                customer.setAge(30);
                customer.setAccountBalance(new java.math.BigDecimal("60000"));
                customer.setExistingLoanOpen(false);
                var result = new com.bank.analyzer.dto.AnalysisResult(request.email(), service.analyze(customer));
                rabbitTemplate.convertAndSend("loan.exchange", "loan.analysis.result", result);
            }
        };

        listener.handle(new AnalysisRequest("integration@example.com"));

        verify(rabbitTemplate).convertAndSend(
                eq("loan.exchange"),
                eq("loan.analysis.result"),
                any(com.bank.analyzer.dto.AnalysisResult.class)
        );
    }
}
