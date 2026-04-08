package com.bank.collector.controller;

import com.bank.collector.dto.DatasetImportResponse;
import com.bank.collector.service.DatasetImportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/collector/import")
public class DatasetImportController {

    private final DatasetImportService datasetImportService;

    public DatasetImportController(DatasetImportService datasetImportService) {
        this.datasetImportService = datasetImportService;
    }

    @PostMapping
    public DatasetImportResponse importDataset() {
        return datasetImportService.importDataset();
    }

    @GetMapping("/summary")
    public Map<String, Long> importSummary() {
        return Map.of("recordCount", datasetImportService.countImportedRecords());
    }
}
