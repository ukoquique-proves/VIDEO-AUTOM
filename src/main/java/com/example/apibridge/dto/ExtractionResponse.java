package com.example.apibridge.dto;

@io.swagger.v3.oas.annotations.media.Schema(description = "Data model representing a stored extraction")
public class ExtractionResponse {
    @io.swagger.v3.oas.annotations.media.Schema(description = "Unique ID of the extraction", example = "1")
    private Long id;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Name of the company involved", example = "Acme Corp")
    private String companyName;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Date of the transaction", example = "2023-01-01")
    private String date;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Total amount of the transaction", example = "123.45")
    private Double totalAmount;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Category of the document", example = "Invoice")
    private String category;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Status mentioned in text", example = "Delayed")
    private String status;

    @io.swagger.v3.oas.annotations.media.Schema(description = "Whether the situation is urgent", example = "true")
    private Boolean isUrgent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
