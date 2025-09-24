package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents an input document with various survey data fields.
 * This class is used to deserialize incoming JSON payloads.
 */
public class InputDocument {

    /**
     * Age of the respondent - < 30, 30 - 60, 60 +
     */
    @Schema(description = "Age of the respondent - < 30, 30 - 60, 60 +", example = "< 30")
    private String age;

    /**
     * Survey date formatted as yyyy-MM-ddTHH:mmZ
     */
    @Schema(description = "Survey date formatted as yyyy-MM-ddTHH:mmZ", example = "2023-03-15T10:00Z")
    private String data;

    /**
     * Gender of the respondent - Male, Female
     */
    @Schema(description = "Gender of the respondent - Male, Female", example = "Male")
    private String gender;

    /**
     * Unique identifier for the document
     */
    @Schema(description = "Unique identifier for the document", example = "12345")
    private String id;

    /**
     * Net Promoter Score (NPS) value ranging from 1 to 10
     */
    @Schema(description = "Net Promoter Score (NPS) value ranging from 1 to 10", example = "9")
    private Integer nps;


    /**
     * NPS group classification: Promoter, Passive, Detractor
     */
    @Schema(description = "NPS group classification: Promoter, Passive, Detractor", example = "Promoter")
    @JsonProperty("nps-group")
    private String npsGroup;

    /**
     * Store feedback text
     */
    @Schema(description = "Store feedback text", example = "Great service and friendly staff!")
    private String text;

    // Getters and Setters
    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNps() {
        return nps;
    }

    public void setNps(Integer nps) {
        this.nps = nps;
    }

    public String getNpsGroup() {
        return npsGroup;
    }

    public void setNpsGroup(String npsGroup) {
        this.npsGroup = npsGroup;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
