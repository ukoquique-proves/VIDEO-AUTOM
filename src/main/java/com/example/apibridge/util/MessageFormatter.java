package com.example.apibridge.util;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionResponse;

public class MessageFormatter {

    public static String formatExtraction(ExtractionResponse res) {
        return format(res.getCompanyName(), res.getDate(), res.getTotalAmount(), 
                      res.getCategory(), res.getStatus(), res.getIsUrgent());
    }

    public static String formatAIExtraction(AIResponse res) {
        return format(res.getCompanyName(), res.getDate(), res.getTotalAmount(), 
                      res.getCategory(), res.getStatus(), res.getIsUrgent());
    }

    private static String format(String company, String date, Double total, 
                                String category, String status, Boolean isUrgent) {
        StringBuilder sb = new StringBuilder();
        if (Boolean.TRUE.equals(isUrgent)) {
            sb.append("⚠️ *URGENT ACTION REQUIRED*\n");
        }
        sb.append("🚀 *Logistics Data Extraction*\n");
        sb.append("----------------------------\n");
        sb.append(String.format("🏢 *Company*: %s\n", company != null ? company : "Unknown"));
        sb.append(String.format("📅 *Date*: %s\n", date != null ? date : "N/A"));
        sb.append(String.format(java.util.Locale.US, "💰 *Total Amount*: $%.2f\n", total != null ? total : 0.0));

        if (category != null) {
            sb.append(String.format("📂 *Category*: %s\n", category));
        }
        if (status != null) {
            sb.append(String.format("🔄 *Status*: %s\n", status));
        }

        sb.append("----------------------------\n");
        sb.append("Processed by AI Logistics Automation Hub");
        return sb.toString();
    }
}
