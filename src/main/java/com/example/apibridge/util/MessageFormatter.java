package com.example.apibridge.util;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionResponse;

public class MessageFormatter {

    public static String formatExtraction(ExtractionResponse extraction) {
        return format(extraction.getCompanyName(), extraction.getDate(), extraction.getTotalAmount());
    }

    public static String formatAIExtraction(AIResponse aiResponse) {
        return format(aiResponse.getCompanyName(), aiResponse.getDate(), aiResponse.getTotalAmount());
    }

    private static String format(String company, String date, Double total) {
        return String.format(
                "🚀 *AI Extraction Complete*\n" +
                        "----------------------------\n" +
                        "🏢 *Company*: %s\n" +
                        "📅 *Date*: %s\n" +
                        "💰 *Total Amount*: $%.2f\n" +
                        "----------------------------\n" +
                        "Processed by The API Bridge",
                company != null ? company : "Unknown",
                date != null ? date : "N/A",
                total != null ? total : 0.0);
    }
}
