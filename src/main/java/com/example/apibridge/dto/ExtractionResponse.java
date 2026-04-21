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
}
