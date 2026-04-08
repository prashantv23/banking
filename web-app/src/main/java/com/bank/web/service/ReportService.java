package com.bank.web.service;

import com.bank.web.dto.AnalysisRequest;
import com.bank.web.dto.AnalysisResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

@Service
public class ReportService {

    private final RabbitTemplate rabbitTemplate;
    private final Map<String, CompletableFuture<AnalysisResult>> pending = new ConcurrentHashMap<>();

    public ReportService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public AnalysisResult requestReport(String email) {
        CompletableFuture<AnalysisResult> future = new CompletableFuture<>();
        pending.put(email, future);
        rabbitTemplate.convertAndSend("loan.exchange", "loan.analysis.request", new AnalysisRequest(email));

        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            pending.remove(email);
            throw new RuntimeException("Interrupted while waiting for analysis result", e);
        } catch (ExecutionException | TimeoutException e) {
            pending.remove(email);
            throw new RuntimeException("Timed out waiting for analysis result", e);
        }
    }

    public void complete(AnalysisResult result) {
        CompletableFuture<AnalysisResult> future = pending.remove(result.email());
        if (future != null) {
            future.complete(result);
        }
    }
}
