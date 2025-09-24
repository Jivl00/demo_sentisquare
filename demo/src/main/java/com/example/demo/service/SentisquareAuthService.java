package com.example.demo.service;

import java.time.Instant;
import java.util.Map;

import com.example.demo.SentisquareProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Service for handling OAuth2 authentication with the Sentisquare API.
 * Manages access token retrieval, caching, and automatic refresh.
 */
@Service
public class SentisquareAuthService {

    /**
     * WebClient instance for making HTTP requests
     */
    private final WebClient webClient;

    /**
     * Sentisquare configuration properties
     */
    private final SentisquareProperties props;

    /**
     * Cached access token - volatile to ensure thread safety
     */
    private volatile String accessToken;

    /**
     * Token expiry time - volatile to ensure thread safety
     */
    private volatile Instant expiry;


    /**
     * Constructor for SentisquareAuthService.
     *
     * @param builder WebClient builder for creating HTTP client
     * @param props   Sentisquare configuration properties
     */
    public SentisquareAuthService(WebClient.Builder builder, SentisquareProperties props) {
        this.webClient = builder.build();
        this.props = props;
    }

    /**
     * Returns a valid access token, refreshing it if necessary.
     * This method is thread-safe and will refresh the token if it's null
     * or expires within the next 30 seconds.
     *
     * @return a valid OAuth2 access token
     */
    public synchronized String getAccessToken() {
        if (accessToken == null || Instant.now().isAfter(expiry.minusSeconds(30))) {
            refreshToken();
        }
        return accessToken;
    }

    /**
     * Refreshes the OAuth2 access token by making a request to the token endpoint.
     * Uses the "password" grant type with client credentials.
     */
    private void refreshToken() {
        // Construct the token endpoint URL
        String tokenEndpoint = props.getBaseUrl()
                + "/auth/realms/sentisquare/protocol/openid-connect/token";

        // Prepare form data for the token request
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("username", props.getUsername());
        form.add("password", props.getPassword());
        form.add("client_id", props.getClientId());
        form.add("client_secret", props.getClientSecret());

        Map<String, Object> response = webClient.post()
                .uri(tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

        // Extract token and expiry from the response
        this.accessToken = (String) response.get("access_token");
        int expiresIn = ((Number) response.get("expires_in")).intValue();
        this.expiry = Instant.now().plusSeconds(expiresIn);
    }
}
