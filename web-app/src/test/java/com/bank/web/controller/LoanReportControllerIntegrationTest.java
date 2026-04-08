package com.bank.web.controller;

import com.bank.web.application.WebApplication;
import com.bank.web.dto.AnalysisResult;
import com.bank.web.dto.LoanOffer;
import com.bank.web.dto.LoanType;
import com.bank.web.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanReportController.class)
@ContextConfiguration(classes = WebApplication.class)
class LoanReportControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReportService reportService;

    @Test
    void shouldReturnLoanReport() throws Exception {
        when(reportService.requestReport("test@example.com"))
                .thenReturn(new AnalysisResult("test@example.com",
                        List.of(new LoanOffer(LoanType.AUTO, new BigDecimal("200000"), 9.5, 60, "eligible"))));

        mockMvc.perform(get("/web/report").param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.offers[0].loanType").value("AUTO"));
    }
}
