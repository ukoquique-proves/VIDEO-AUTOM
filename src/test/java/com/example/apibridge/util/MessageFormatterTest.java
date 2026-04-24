package com.example.apibridge.util;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageFormatterTest {

    @Test
    public void testFormatExtraction() {
        ExtractionResponse extraction = new ExtractionResponse();
        extraction.setCompanyName("Test Corp");
        extraction.setDate("2023-10-10");
        extraction.setTotalAmount(1500.0);

        String result = MessageFormatter.formatExtraction(extraction);

        assertTrue(result.contains("Test Corp"));
        assertTrue(result.contains("2023-10-10"));
        assertTrue(result.contains("$1500.00"));
        assertTrue(result.contains("Logistics Data Extraction"));
    }

    @Test
    public void testFormatAIExtraction() {
        AIResponse aiResponse = new AIResponse();
        aiResponse.setCompanyName("AI Logistics");
        aiResponse.setDate("2024-01-01");
        aiResponse.setTotalAmount(250.75);

        String result = MessageFormatter.formatAIExtraction(aiResponse);

        assertTrue(result.contains("AI Logistics"));
        assertTrue(result.contains("2024-01-01"));
        assertTrue(result.contains("$250.75"));
        assertTrue(result.contains("Logistics Data Extraction"));
    }

    @Test
    public void testFormatWithNullFields() {
        AIResponse aiResponse = new AIResponse();
        aiResponse.setCompanyName(null);
        aiResponse.setDate(null);
        aiResponse.setTotalAmount(null);

        String result = MessageFormatter.formatAIExtraction(aiResponse);

        assertTrue(result.contains("Unknown"));
        assertTrue(result.contains("N/A"));
        assertTrue(result.contains("$0.00"));
    }
}
