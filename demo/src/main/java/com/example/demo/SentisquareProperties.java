package com.example.demo;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Sentisquare API access.
 * * These are set in application.yml.
 */
@Component
@ConfigurationProperties(prefix = "sentisquare")
public class SentisquareProperties {
    /**
     * Base URL for the Sentisquare API
     */
    private String baseUrl;

    /**
     * OAuth2 client ID for API authentication
     */
    private String clientId;

    /**
     * OAuth2 client secret for API authentication
     */
    private String clientSecret;

    /**
     * Username for API access
     */
    private String username;

    /**
     * Password for API access
     */
    private String password;

    /**
     * Index alias for API operations
     */
    private String indexAlias;

    /**
     * Classifier aliases mapping - allows mapping of classifier names to different identifiers
     */
    private Map<String, String> classifierAliases;

    // Getters and setters
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIndexAlias() {
        return indexAlias;
    }

    public void setIndexAlias(String indexAlias) {
        this.indexAlias = indexAlias;
    }

    public Map<String, String> getClassifierAliases() {
        return classifierAliases;
    }

    public void setClassifierAliases(Map<String, String> classifierAliases) {
        this.classifierAliases = classifierAliases;
    }
}
