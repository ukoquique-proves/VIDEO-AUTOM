package com.example.apibridge.service;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service responsible for orchestrating the AI extraction logic.
 * <p>
 * This service coordinates with external LLM providers (e.g., Groq) to convert 
 * unstructured text into structured {@link AIResponse} objects. It does not 
 * handle data persistence; that is the responsibility of {@link ExtractionService}.
 * </p>
 */
@Service
public class AIService {

    private static final Logger log = LoggerFactory.getLogger(AIService.class);

    private final String groqApiKey;
    private final String groqModel;
    private final String groqApiUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AIService(@Value("${groq.api.key}") String groqApiKey,
            @Value("${groq.model}") String groqModel,
            @Value("${groq.api.url}") String groqApiUrl,
            RestTemplate restTemplate,
            ObjectMapper objectMapper) {
        this.groqApiKey = groqApiKey;
        this.groqModel = groqModel;
        this.groqApiUrl = groqApiUrl;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public AIResponse extractData(ExtractionRequest request) {
        if (request == null || request.getText() == null || request.getText().trim().isEmpty()) {
            log.error("Aborting: Input text is empty or request is null");
            throw new IllegalArgumentException("Input text cannot be empty for AI extraction.");
        }

        log.info("Starting AI extraction for text length: {}", request.getText().length());

        String prompt = "Extract data from the following logistics text. Respond ONLY with a valid JSON object containing the fields: 'companyName', 'date' (YYYY-MM-DD), 'totalAmount' (numeric), 'category' (e.g., Invoice, Status Update), 'status' (e.g., Delayed, Delivered, Pending), and 'isUrgent' (boolean).\n\n"
                + "IMPORTANT: If the text contains multiple entries, consolidate the information into a SINGLE flat JSON object (do not use arrays). Summarize the overall status and total amounts where applicable.\n\n"
                + "If a field is not found or not applicable, use null.\n\nText: "
                + request.getText();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", groqModel);

        ArrayNode messages = requestBody.putArray("messages");
        messages.addObject()
                .put("role", "system")
                .put("content",
                        "You are a specialized data extraction assistant. You always respond with pure JSON, no conversational text.");
        messages.addObject()
                .put("role", "user")
                .put("content", prompt);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            log.info("Calling Groq API (Model: {})...", groqModel);
            String response = restTemplate.postForObject(groqApiUrl, entity, String.class);
            log.info("Groq API call successful.");

            com.fasterxml.jackson.databind.JsonNode rootNode = objectMapper.readTree(response);
            String content = rootNode.path("choices").get(0).path("message").path("content").asText();

            // Strip Markdown code blocks if present
            if (content.startsWith("```json")) {
                content = content.substring(7);
            } else if (content.startsWith("```")) {
                content = content.substring(3);
            }
            if (content.endsWith("```")) {
                content = content.substring(0, content.length() - 3);
            }
            // Trim whitespace
            content = content.trim();

            AIResponse result = objectMapper.readValue(content, AIResponse.class);
            log.info("Successfully extracted data for company: {}", result.getCompanyName());
            return result;
        } catch (Exception e) {
            log.error("AI Extraction failed: {}", e.getMessage());
            throw new RuntimeException("AI Extraction failed: " + e.getMessage(), e);
        }
    }
}
