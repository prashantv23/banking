package com.bank.collector.support.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "banking.dataset")
public class DatasetImportProperties {

    private boolean importOnStartup = true;
    private String metadataUrl;

    public boolean isImportOnStartup() {
        return importOnStartup;
    }

    public void setImportOnStartup(boolean importOnStartup) {
        this.importOnStartup = importOnStartup;
    }

    public String getMetadataUrl() {
        return metadataUrl;
    }

    public void setMetadataUrl(String metadataUrl) {
        this.metadataUrl = metadataUrl;
    }
}
