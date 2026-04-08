package com.bank.web.service;

import com.bank.web.dto.AnalysisResult;
import com.bank.web.dto.LoanOffer;
import com.bank.web.dto.LoanType;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Test
    void shouldReturnResultWhenCompletedLater() throws Exception {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        ReportService service = new ReportService(rabbitTemplate);

        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            service.complete(new AnalysisResult("a@b.com",
                    List.of(new LoanOffer(LoanType.PERSONAL, new BigDecimal("100000"), 12.0, 36, "ok"))));
        });
        worker.start();

        AnalysisResult result = service.requestReport("a@b.com");

        assertEquals("a@b.com", result.email());
        assertEquals(1, result.offers().size());
        verify(rabbitTemplate).convertAndSend(
                eq("loan.exchange"),
                eq("loan.analysis.request"),
                any(com.bank.web.dto.AnalysisRequest.class)
        );
    }
}
