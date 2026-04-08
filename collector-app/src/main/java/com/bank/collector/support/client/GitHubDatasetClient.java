package com.bank.collector.support.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class GitHubDatasetClient {

    private final RestTemplate restTemplate;

    public GitHubDatasetClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchDatasetCsv(String metadataUrl) {
        GitHubContentResponse metadata = restTemplate.getForObject(metadataUrl, GitHubContentResponse.class);
        if (metadata == null || metadata.downloadUrl() == null || metadata.downloadUrl().isBlank()) {
            throw new RestClientException("GitHub dataset metadata did not include a download URL");
        }

        return restTemplate.getForObject(metadata.downloadUrl(), String.class);
    }

    public record GitHubContentResponse(@JsonProperty("download_url") String downloadUrl) {
    }
}
