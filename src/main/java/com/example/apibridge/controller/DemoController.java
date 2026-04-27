package com.example.apibridge.controller;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionRequest;
import com.example.apibridge.service.AIService;
import com.example.apibridge.service.ExtractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Profile("demo")
@RestController
@RequestMapping("/api/demo")
@Tag(name = "Demo Operations", description = "Utilities for video demonstrations")
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);
    private final AIService aiService;
    private final ExtractionService extractionService;

    public DemoController(AIService aiService, ExtractionService extractionService) {
        this.aiService = aiService;
        this.extractionService = extractionService;
    }

    @GetMapping("/health")
    @Operation(summary = "Demo profile health check", description = "Returns 200 when the demo profile is active. Used by record.js to confirm the server is ready for recording.")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("demo profile active");
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset Database", description = "Deletes all extractions and ensures a clean state for the demo.")
    public ResponseEntity<String> resetDatabase() {
        extractionService.clearAll();
        return ResponseEntity.ok("Database reset successful. Ready for demo!");
    }

    @PostMapping("/populate")
    @Operation(summary = "Populate Demo Data", description = "Reads sample texts from demo-assets and processes them through the AI pipeline.")
    public ResponseEntity<String> populateDemoData() {
        Path demoAssetsDir = Paths.get("demo-assets");
        if (!Files.exists(demoAssetsDir)) {
            return ResponseEntity.internalServerError().body("demo-assets directory not found");
        }

        List<Path> demoFiles;
        try (Stream<Path> paths = Files.list(demoAssetsDir)) {
            demoFiles = paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to list demo assets: " + e.getMessage());
        }

        if (demoFiles.isEmpty()) {
            return ResponseEntity.internalServerError().body("No demo .txt files found in demo-assets");
        }

        int successful = 0;
        List<String> failures = new ArrayList<>();

        for (Path path : demoFiles) {
            try {
                String content = Files.readString(path);
                log.info("Processing demo file: {}", path.getFileName());
                ExtractionRequest request = new ExtractionRequest();
                request.setText(content);
                AIResponse aiResponse = aiService.extractData(request);
                extractionService.saveAIExtraction(aiResponse);
                successful++;
            } catch (Exception e) {
                log.error("Failed to process demo file {}: {}", path, e.getMessage());
                failures.add(path.getFileName() + ": " + e.getMessage());
            }
        }

        if (successful == 0) {
            String reason = failures.isEmpty() ? "Unknown error" : failures.get(0);
            return ResponseEntity.internalServerError()
                    .body("No demo records were created. First error: " + reason);
        }

        String summary = "Demo populate finished. Created " + successful + " of " + demoFiles.size() + " records.";
        if (!failures.isEmpty()) {
            int previewCount = Math.min(3, failures.size());
            summary += " Failures: " + String.join(" | ", failures.subList(0, previewCount));
        }
        return ResponseEntity.ok(summary);
    }
}
