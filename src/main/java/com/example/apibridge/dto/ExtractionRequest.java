package com.example.apibridge.dto;

@io.swagger.v3.oas.annotations.media.Schema(description = "Request body for AI extraction containing raw text")
public class ExtractionRequest {
    @io.swagger.v3.oas.annotations.media.Schema(description = "Raw text input for AI extraction", example = "Invoice from Acme Corp for $2450.50 on Jan 20, 2026")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
