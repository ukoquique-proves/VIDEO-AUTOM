package com.example.apibridge.service;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionResponse;
import com.example.apibridge.exception.ResourceNotFoundException;
import com.example.apibridge.mapper.ExtractionMapper;
import com.example.apibridge.model.Extraction;
import com.example.apibridge.repository.ExtractionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExtractionFetchServiceTest {

    @Mock
    private ExtractionRepository extractionRepository;

    @Mock
    private ExtractionMapper extractionMapper;

    @InjectMocks
    private ExtractionFetchService extractionFetchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveAIExtraction() {
        AIResponse aiResponse = new AIResponse();
        Extraction extraction = new Extraction();
        Extraction savedExtraction = new Extraction();
        ExtractionResponse expectedResponse = new ExtractionResponse();

        when(extractionMapper.toEntity(aiResponse)).thenReturn(extraction);
        when(extractionRepository.save(extraction)).thenReturn(savedExtraction);
        when(extractionMapper.toDto(savedExtraction)).thenReturn(expectedResponse);

        ExtractionResponse actualResponse = extractionFetchService.saveAIExtraction(aiResponse);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(extractionRepository, times(1)).save(extraction);
    }

    @Test
    public void testFetchAllExtractions() {
        Extraction e1 = new Extraction();
        Extraction e2 = new Extraction();
        when(extractionRepository.findAll()).thenReturn(Arrays.asList(e1, e2));
        when(extractionMapper.toDto(any(Extraction.class))).thenReturn(new ExtractionResponse());

        List<ExtractionResponse> results = extractionFetchService.fetchAllExtractions();

        assertEquals(2, results.size());
        verify(extractionMapper, times(2)).toDto(any(Extraction.class));
    }

    @Test
    public void testFetchExtractionByIdFound() {
        Long id = 1L;
        Extraction extraction = new Extraction();
        ExtractionResponse expectedResponse = new ExtractionResponse();

        when(extractionRepository.findById(id)).thenReturn(Optional.of(extraction));
        when(extractionMapper.toDto(extraction)).thenReturn(expectedResponse);

        ExtractionResponse actualResponse = extractionFetchService.fetchExtractionById(id);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testFetchExtractionByIdNotFoundThrowsException() {
        Long id = 99L;
        when(extractionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            extractionFetchService.fetchExtractionById(id);
        });
    }
}
