package com.example.demo.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.SentisquareProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service to interact with Sentisquare Classifier API for text classification tasks.
 * Provides methods for sentiment analysis and topic classification.
 */
@Service
public class SentisquareClassifierService {

    /**
     * WebClient instance for making HTTP requests to the Sentisquare API
     */
    private final WebClient webClient = WebClient.builder().build();

    /**
     * ObjectMapper for parsing JSON responses from the API
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Sentisquare configuration properties
     */
    private final SentisquareProperties sentisquareProperties;

    public SentisquareClassifierService(SentisquareProperties sentisquareProperties) {
        this.sentisquareProperties = sentisquareProperties;
    }

    /**
     * Classifies the sentiment of the given text using Sentisquare API.
     * Returns the selected sentiment category from the classification result.
     *
     * @param token           the OAuth2 access token for API authentication
     * @param text            the text to classify for sentiment
     * @param classifierAlias the alias/ID of the sentiment classifier to use
     * @return the classified sentiment as a string, or "unknown" if classification fails
     */
    public String classifySentiment(String token, String text, String classifierAlias) {
        String response = callClassifier(token, text, classifierAlias);

        try {
            // Parse the JSON response to extract sentiment information
            JsonNode root = objectMapper.readTree(response);
            JsonNode categories = root.path("documentClassification").path("categories");
            JsonNode selected = root.path("documentClassification").path("selected");

            for (int i = 0; i < selected.size(); i++) {
                if (selected.get(i).asBoolean()) {
                    return categories.get(i).asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * Classifies topics in the given text using Sentisquare API.
     * Returns all selected topic categories from the classification result.
     *
     * @param token           the OAuth2 access token for API authentication
     * @param text            the text to classify for topics
     * @param classifierAlias the alias/ID of the topic classifier to use
     * @return a list of classified topics, or empty list if classification fails or none are selected
     */
    public List<String> classifyTopics(String token, String text, String classifierAlias) {
        String response = callClassifier(token, text, classifierAlias);
        List<String> topics = new ArrayList<>();

        try {
            // Parse the JSON response to extract topic information
            JsonNode root = objectMapper.readTree(response);
            JsonNode categories = root.path("documentClassification").path("categories");
            JsonNode selected = root.path("documentClassification").path("selected");

            for (int i = 0; i < selected.size(); i++) {
                if (selected.get(i).asBoolean()) {
                    topics.add(categories.get(i).asText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topics;
    }

    /**
     * Makes a POST request to the Sentisquare classifier API endpoint.
     * This is a private helper method used by both sentiment and topic classification methods.
     *
     * @param token           the OAuth2 access token for API authentication
     * @param text            the text to be classified
     * @param classifierAlias the alias/ID of the classifier to use
     * @return the raw JSON response from the API as a string
     */
    private String callClassifier(String token, String text, String classifierAlias) {
        // Construct the API URL using the provided classifier alias
        String url = sentisquareProperties.getBaseUrl() + "/api/classifier/" + classifierAlias + "/classify/";
        Map<String, Object> body = Map.of("text", text);

        return webClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
