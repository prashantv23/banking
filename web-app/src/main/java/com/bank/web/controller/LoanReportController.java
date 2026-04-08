package com.bank.web.controller;

import com.bank.web.dto.AnalysisResult;
import com.bank.web.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web")
public class LoanReportController {

    private final ReportService reportService;

    public LoanReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report")
    public AnalysisResult report(@RequestParam String email) {
        return reportService.requestReport(email);
    }
}
