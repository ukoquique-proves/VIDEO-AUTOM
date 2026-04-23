package com.example.apibridge.mapper;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionResponse;
import com.example.apibridge.model.Extraction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtractionMapperTest {

    private final ExtractionMapper mapper = new ExtractionMapper();

    @Test
    public void testToDto() {
        Extraction extraction = new Extraction();
        extraction.setId(1L);
        extraction.setCompanyName("Test Corp");
        extraction.setDate("2023-10-10");
        extraction.setTotalAmount(150.0);

        ExtractionResponse dto = mapper.toDto(extraction);

        assertEquals(1L, dto.getId());
        assertEquals("Test Corp", dto.getCompanyName());
        assertEquals("2023-10-10", dto.getDate());
        assertEquals(150.0, dto.getTotalAmount());
    }

    @Test
    public void testToEntity() {
        AIResponse aiResponse = new AIResponse();
        aiResponse.setCompanyName("AI Logistics");
        aiResponse.setDate("2024-01-01");
        aiResponse.setTotalAmount(250.75);

        Extraction entity = mapper.toEntity(aiResponse);

        assertEquals("AI Logistics", entity.getCompanyName());
        assertEquals("2024-01-01", entity.getDate());
        assertEquals(250.75, entity.getTotalAmount());
    }
}
