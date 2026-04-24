package com.example.apibridge.controller;

import com.example.apibridge.dto.ExtractionResponse;
import com.example.apibridge.service.ExtractionFetchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/extractions")
@Tag(name = "Extraction Retrieval", description = "Endpoints for viewing stored extraction data")
public class ExtractionController {

    private final ExtractionFetchService extractionFetchService;

    public ExtractionController(ExtractionFetchService extractionFetchService) {
        this.extractionFetchService = extractionFetchService;
    }

    @GetMapping
    @Operation(summary = "Get all extractions", description = "Returns a list of all data extracted from documents.")
    public List<ExtractionResponse> getAllExtractions() {
        return extractionFetchService.fetchAllExtractions();
    }
}
