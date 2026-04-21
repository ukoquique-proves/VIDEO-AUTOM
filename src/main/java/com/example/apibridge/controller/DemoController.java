package com.example.apibridge.controller;

import com.example.apibridge.repository.ExtractionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
@Tag(name = "Demo Operations", description = "Utilities for video demonstrations")
public class DemoController {

    private final ExtractionRepository repository;

    @Autowired
    public DemoController(ExtractionRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset Database", description = "Deletes all extractions and ensures a clean state for the demo.")
    public ResponseEntity<String> resetDatabase() {
        repository.deleteAll();
        return ResponseEntity.ok("Database reset successful. Ready for demo!");
    }
}
