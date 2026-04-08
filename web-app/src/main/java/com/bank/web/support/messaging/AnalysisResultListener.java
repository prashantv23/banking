package com.bank.web.support.messaging;

import com.bank.web.dto.AnalysisResult;
import com.bank.web.service.ReportService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AnalysisResultListener {

    private final ReportService reportService;

    public AnalysisResultListener(ReportService reportService) {
        this.reportService = reportService;
    }

    @RabbitListener(queues = "loan.analysis.result")
    public void handle(AnalysisResult result) {
        reportService.complete(result);
    }
}
