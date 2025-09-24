package com.example.demo;

/**
 * Represents an output document that extends the input document with additional
 * fields obtained from API classification.
 * This class is used to serialize the enriched document to JSON.
 */
public class OutputDocument extends InputDocument {
    // API enrichment fields

    /**
     * Sentiment classification result - Positive,  Negative
     */
    private String sentiment;

    /**
     * List of topic classification results
     */
    private java.util.List<String> topics;

    // Getters and Setters
    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public java.util.List<String> getTopics() {
        return topics;
    }

    public void setTopics(java.util.List<String> topics) {
        this.topics = topics;
    }
}
