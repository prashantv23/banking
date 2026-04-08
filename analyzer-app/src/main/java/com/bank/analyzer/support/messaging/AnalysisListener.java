package com.bank.analyzer.support.messaging;

import com.bank.analyzer.dto.AnalysisRequest;
import com.bank.analyzer.dto.AnalysisResult;
import com.bank.analyzer.dto.Customer;
import com.bank.analyzer.service.LoanEligibilityService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AnalysisListener {

    private final LoanEligibilityService loanEligibilityService;
    private final RabbitTemplate rabbitTemplate;
    private final RestClient collectorClient;

    public AnalysisListener(
            LoanEligibilityService loanEligibilityService,
            RabbitTemplate rabbitTemplate,
            @Value("${collector.base-url}") String collectorBaseUrl
    ) {
        this.loanEligibilityService = loanEligibilityService;
        this.rabbitTemplate = rabbitTemplate;
        this.collectorClient = RestClient.builder().baseUrl(collectorBaseUrl).build();
    }

    @RabbitListener(queues = "loan.analysis.request")
    public void handle(AnalysisRequest request) {
        Customer customer = collectorClient.get()
                .uri(uriBuilder -> uriBuilder.path("/customer").queryParam("email", request.email()).build())
                .retrieve()
                .body(Customer.class);

        AnalysisResult result = new AnalysisResult(request.email(), loanEligibilityService.analyze(customer));
        rabbitTemplate.convertAndSend("loan.exchange", "loan.analysis.result", result);
    }
}
