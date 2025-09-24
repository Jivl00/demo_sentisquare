

# Customer Feedback Processing API

This Java Spring Boot service exposes a REST API that receives customer feedback,
analyzes it using Sentisquare’s No-Code NLP platform, and stores the enriched
results for further reporting and dashboards.

## Project Overview

The goal of the overall project is to:
- **Classify sentiment** of customer feedback (positive / negative / neutral).
- **Identify feedback topics** (multiple topics per text).
- Automate the process so new feedback is analyzed and indexed automatically.

This Java component provides the integration layer between external feedback
sources and the Sentisquare NLP platform.

## Features

- **Single REST endpoint** that accepts a JSON payload representing one customer
  feedback record (text plus metadata such as age, gender, etc.).
- Calls Sentisquare APIs for:
  - **Sentiment classification**  
  - **Topic classification**  
  - **Indexing results** into a searchable store.
- **Swagger/OpenAPI documentation** available at  
  `http://localhost:8080/swagger-ui.html`.

## Tech Stack

- Java 21+
- Spring Boot (Web, Validation, OpenAPI/Swagger)
- Maven (build & dependency management)