package com.bank.collector.support.config;

import com.bank.collector.service.DatasetImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableConfigurationProperties(DatasetImportProperties.class)
public class StartupImportConfig {

    @Bean
    CommandLineRunner importBankingDataset(DatasetImportService datasetImportService,
                                           DatasetImportProperties properties) {
        return args -> {
            if (properties.isImportOnStartup()) {
                datasetImportService.importDataset();
            }
        };
    }
}
