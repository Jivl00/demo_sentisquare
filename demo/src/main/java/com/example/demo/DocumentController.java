package com.example.demo;

import com.example.demo.service.SentisquareAuthService;
import com.example.demo.service.SentisquareClassifierService;
import com.example.demo.service.SentisquareDocumentIndexService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * Controller for handling document-related API requests.
 * This class provides endpoints for receiving documents and processing them
 * using Sentisquare's classification services.
 */
@SpringBootApplication
@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "API for handling incoming documents")
public class DocumentController {

    /**
     * Service for handling OAuth2 authentication with Sentisquare API
     */
    private final SentisquareAuthService oAuthService;

    /**
     * Service for classifying text using Sentisquare API
     */
    private final SentisquareClassifierService classifierService;

    /**
     * Configuration properties for Sentisquare API access
     */
    private final SentisquareProperties sentisquareProperties;

    /**
     * Service for saving documents to Sentisquare Document Index
     */
    @Autowired
    private SentisquareDocumentIndexService documentIndexService;


    /**
     * Constructor for DocumentController.
     *
     * @param oAuthService          the OAuth2 authentication service
     * @param classifierService     the text classification service
     * @param sentisquareProperties the configuration properties for Sentisquare API
     */
    public DocumentController(SentisquareAuthService oAuthService,
                              SentisquareClassifierService classifierService,
                              SentisquareProperties sentisquareProperties) {
        this.oAuthService = oAuthService;
        this.classifierService = classifierService;
        this.sentisquareProperties = sentisquareProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(DocumentController.class, args);
    }

    /**
     * Endpoint to receive a document and classify its content.
     *
     * @param doc the input document containing survey data
     * @return an OutputDocument containing classified results and metadata
     */
    @PostMapping
    @Operation(
            summary = "Receive and classify a document",
            description = "Receives a document containing survey data, classifies its sentiment and topics using the Sentisquare API, and returns the classified results along with the original document metadata.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Input document containing survey data",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InputDocument.class)
                    )
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Classified document and save confirmation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "object",
                                    additionalProperties = Schema.AdditionalPropertiesValue.FALSE,
                                    example = """
                                            {
                                              "document": {
                                                "id": "123",
                                                "text": "Sample text",
                                                "sentiment": "positive",
                                                "topics": ["Staff", "Cleanliness"],
                                                "etc": "... other metadata fields ..."
                                              },
                                              "response": "[{\\"docId\\":\\"123\\",\\"validationErrors\\":[]}]"
                                            }"""
                            )
                    )
            )
    )
    public Map<String, Object> receive(@RequestBody InputDocument doc) {

        String token = oAuthService.getAccessToken();
        String sentiment_alias = sentisquareProperties.getClassifierAliases().get("sentiment");
        String topic_alias = sentisquareProperties.getClassifierAliases().get("topic");

        // Classify sentiment and topics using Sentisquare API
        String sentiment = classifierService.classifySentiment(token, doc.getText(), sentiment_alias);
        java.util.List<String> topics = classifierService.classifyTopics(token, doc.getText(), topic_alias);

        // Create OutputDocument with original metadata and classification results
        OutputDocument outputDoc = new OutputDocument() {{
            setText(doc.getText());
            setId(doc.getId());
            setAge(doc.getAge());
            setGender(doc.getGender());
            setNps(doc.getNps());
            setNpsGroup(doc.getNpsGroup());
            setData(doc.getData());
            setSentiment(sentiment);
            setTopics(topics);
        }};

        // Save the OutputDocument to the specified index
        String response = documentIndexService.saveDocumentToIndex(token, sentisquareProperties.getIndexAlias(), outputDoc);
        System.out.println("Document indexed. Response: " + response);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("document", outputDoc);
        responseMap.put("response", response);

        return responseMap;
    }
}