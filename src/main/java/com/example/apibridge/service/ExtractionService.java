package com.example.apibridge.service;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionResponse;
import com.example.apibridge.exception.ResourceNotFoundException;
import com.example.apibridge.mapper.ExtractionMapper;
import com.example.apibridge.model.Extraction;
import com.example.apibridge.repository.ExtractionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for the persistence and retrieval of extraction data.
 * <p>
 * While {@link AIService} handles the logic of extracting data from raw text 
 * using external LLMs, {@code ExtractionService} acts as the storage layer, 
 * managing the database operations for the resulting structured data.
 * </p>
 */
@Service
public class ExtractionService {
    private final ExtractionRepository extractionRepository;
    private final ExtractionMapper extractionMapper;

    public ExtractionService(ExtractionRepository extractionRepository, ExtractionMapper extractionMapper) {
        this.extractionRepository = extractionRepository;
        this.extractionMapper = extractionMapper;
    }

    public ExtractionResponse saveAIExtraction(AIResponse aiResponse) {
        Extraction extraction = extractionMapper.toEntity(aiResponse);
        Extraction saved = extractionRepository.save(extraction);
        return extractionMapper.toDto(saved);
    }

    public List<ExtractionResponse> fetchAllExtractions() {
        List<Extraction> extractions = extractionRepository.findAll();
        return extractions.stream().map(extractionMapper::toDto).collect(Collectors.toList());
    }

    public void clearAll() {
        extractionRepository.deleteAll();
    }

    public ExtractionResponse fetchExtractionById(Long id) {
        return extractionRepository.findById(id)
                .map(extractionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Extraction not found with ID: " + id));
    }
}
