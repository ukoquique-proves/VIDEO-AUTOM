package com.example.apibridge.mapper;

import com.example.apibridge.dto.ExtractionResponse;
import com.example.apibridge.model.Extraction;
import org.springframework.stereotype.Component;

@Component
public class ExtractionMapper {
    public ExtractionResponse toDto(Extraction extraction) {
        ExtractionResponse dto = new ExtractionResponse();
        dto.setId(extraction.getId());
        dto.setCompanyName(extraction.getCompanyName());
        dto.setDate(extraction.getDate());
        dto.setTotalAmount(extraction.getTotalAmount());
        return dto;
    }

    public Extraction toEntity(com.example.apibridge.dto.AIResponse aiResponse) {
        Extraction extraction = new Extraction();
        extraction.setCompanyName(aiResponse.getCompanyName());
        extraction.setDate(aiResponse.getDate());
        extraction.setTotalAmount(aiResponse.getTotalAmount());
        return extraction;
    }
}
