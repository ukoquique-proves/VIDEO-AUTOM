package com.example.apibridge.dto;

@io.swagger.v3.oas.annotations.media.Schema(description = "Structured data extracted by AI")
public class AIResponse {
    @io.swagger.v3.oas.annotations.media.Schema(description = "Extracted company or entity name", example = "Acme Corp")
    private String companyName;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Extracted date in YYYY-MM-DD format", example = "2023-01-01")
    private String date;

    @com.fasterxml.jackson.annotation.JsonProperty("totalAmount")
    @io.swagger.v3.oas.annotations.media.Schema(description = "Extracted total amount", example = "500.00")
    private Double totalAmount;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
