package com.example.apibridge.dto;

@io.swagger.v3.oas.annotations.media.Schema(description = "Structured data extracted by AI")
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class AIResponse {
    @io.swagger.v3.oas.annotations.media.Schema(description = "Extracted company or entity name", example = "Acme Corp")
    private String companyName;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Extracted date in YYYY-MM-DD format", example = "2023-01-01")
    private String date;

    @com.fasterxml.jackson.annotation.JsonProperty("totalAmount")
    @io.swagger.v3.oas.annotations.media.Schema(description = "Extracted total amount", example = "500.00")
    private Double totalAmount;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Category of the document (e.g. Invoice, Status Update)", example = "Invoice")
    private String category;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Status mentioned in text (e.g. Delayed, Delivered)", example = "Delayed")
    private String status;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Whether the text indicates an urgent situation", example = "true")
    private Boolean isUrgent;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Boolean urgent) {
        isUrgent = urgent;
    }
}
