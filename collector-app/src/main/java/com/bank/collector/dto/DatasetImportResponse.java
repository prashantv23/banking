package com.bank.collector.dto;

public record DatasetImportResponse(
        int importedRecords,
        boolean skipped,
        String source
) {
}
