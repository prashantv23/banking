package com.bank.collector.support.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "loan.exchange";
    public static final String ANALYSIS_REQUEST_QUEUE = "loan.analysis.request";
    public static final String ANALYSIS_RESULT_QUEUE = "loan.analysis.result";

    @Bean
    DirectExchange loanExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    Queue analysisRequestQueue() {
        return new Queue(ANALYSIS_REQUEST_QUEUE, true);
    }

    @Bean
    Queue analysisResultQueue() {
        return new Queue(ANALYSIS_RESULT_QUEUE, true);
    }

    @Bean
    Binding requestBinding() {
        return BindingBuilder.bind(analysisRequestQueue()).to(loanExchange()).with(ANALYSIS_REQUEST_QUEUE);
    }

    @Bean
    Binding resultBinding() {
        return BindingBuilder.bind(analysisResultQueue()).to(loanExchange()).with(ANALYSIS_RESULT_QUEUE);
    }
}
