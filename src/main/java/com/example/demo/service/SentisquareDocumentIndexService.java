package com.example.demo.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.demo.OutputDocument;
import com.example.demo.SentisquareProperties;


/**
 * Service to interact with Sentisquare Document Index API for saving documents.
 * Provides methods to save classified documents to a specified index.
 */
@Service
public class SentisquareDocumentIndexService {

    /**
     * WebClient instance for making HTTP requests to the Sentisquare API
     */
    private final WebClient webClient = WebClient.builder().build();
    /**
     * Sentisquare configuration properties
     */
    private final SentisquareProperties sentisquareProperties;

    public SentisquareDocumentIndexService(SentisquareProperties sentisquareProperties) {
        this.sentisquareProperties = sentisquareProperties;
    }

    /**
     * Saves the OutputDocument to the specified index.
     *
     * @param token the OAuth2 access token for authentication
     * @param alias the index alias to save the document to
     * @param document the OutputDocument to save
     * @return the response from the API
     */
    public String saveDocumentToIndex(String token, String alias, OutputDocument document) {
        String url = String.format("%s/api/data/%s/save-documents/", sentisquareProperties.getBaseUrl(), alias);

        java.util.List<OutputDocument> documents = java.util.Collections.singletonList(document);
        return webClient.post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(documents)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}