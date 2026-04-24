package com.example.apibridge.util;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionResponse;

public class MessageFormatter {

    public static String formatExtraction(ExtractionResponse extraction) {
        return format(extraction.getCompanyName(), extraction.getDate(), extraction.getTotalAmount());
    }

    public static String formatAIExtraction(AIResponse res) {
        StringBuilder sb = new StringBuilder();
        if (Boolean.TRUE.equals(res.getIsUrgent())) {
            sb.append("⚠️ *URGENT ACTION REQUIRED*\n");
        }
        sb.append("🚀 *Logistics Data Extraction*\n");
        sb.append("----------------------------\n");
        sb.append(String.format("🏢 *Company*: %s\n", res.getCompanyName() != null ? res.getCompanyName() : "Unknown"));
        sb.append(String.format("📅 *Date*: %s\n", res.getDate() != null ? res.getDate() : "N/A"));
        sb.append(String.format(java.util.Locale.US, "💰 *Total Amount*: $%.2f\n", res.getTotalAmount() != null ? res.getTotalAmount() : 0.0));
        
        if (res.getCategory() != null) {
            sb.append(String.format("📂 *Category*: %s\n", res.getCategory()));
        }
        if (res.getStatus() != null) {
            sb.append(String.format("🔄 *Status*: %s\n", res.getStatus()));
        }
        
        sb.append("----------------------------\n");
        sb.append("Processed by The API Bridge");
        return sb.toString();
    }

    private static String format(String company, String date, Double total) {
        return String.format(java.util.Locale.US,
                "🚀 *Logistics Data Extraction*\n" +
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
