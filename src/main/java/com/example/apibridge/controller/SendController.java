package com.example.apibridge.controller;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionRequest;
import com.example.apibridge.dto.ExtractionResponse;
import com.example.apibridge.service.AIService;
import com.example.apibridge.service.EmailSenderService;
import com.example.apibridge.service.ExtractionFetchService;
import com.example.apibridge.service.SlackSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/send")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Sending Operations", description = "Endpoints for triggering email and Slack notifications")
public class SendController {
    private final ExtractionFetchService extractionFetchService;
    private final EmailSenderService emailSenderService;
    private final SlackSenderService slackSenderService;
    private final AIService aiService;
    private final com.example.apibridge.repository.ExtractionRepository extractionRepository;
    private final com.example.apibridge.mapper.ExtractionMapper extractionMapper;

    @Autowired
    public SendController(ExtractionFetchService extractionFetchService,
            EmailSenderService emailSenderService,
            SlackSenderService slackSenderService,
            AIService aiService,
            com.example.apibridge.repository.ExtractionRepository extractionRepository,
            com.example.apibridge.mapper.ExtractionMapper extractionMapper) {
        this.extractionFetchService = extractionFetchService;
        this.emailSenderService = emailSenderService;
        this.slackSenderService = slackSenderService;
        this.aiService = aiService;
        this.extractionRepository = extractionRepository;
        this.extractionMapper = extractionMapper;
    }

    @PostMapping("/email/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Send existing extraction to email", description = "Sends a pre-existing extraction record (by ID) to the specified email address.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Email sent successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Extraction not found")
    public ResponseEntity<String> sendToEmail(@PathVariable Long id, @RequestParam String to) {
        ExtractionResponse extraction = extractionFetchService.fetchExtractionById(id);
        emailSenderService.sendExtractionByEmail(to, extraction);
        return ResponseEntity.ok("Sent to email");
    }

    @PostMapping("/slack/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Send existing extraction to Slack", description = "Sends a pre-existing extraction record (by ID) to the configured Slack channel.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Message sent to Slack successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Extraction not found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<String> sendToSlack(@PathVariable Long id) {
        ExtractionResponse extraction = extractionFetchService.fetchExtractionById(id);
        slackSenderService.sendExtractionToSlack(extraction);
        return ResponseEntity.ok("Sent to Slack");
    }

    @PostMapping("/ai/email")
    @io.swagger.v3.oas.annotations.Operation(summary = "Extract and send to email", description = "Extracts data from raw text using AI and sends it to an email.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Extraction sent to email")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to extract data")
    public ResponseEntity<String> sendAIExtractionToEmail(@RequestBody ExtractionRequest request,
            @RequestParam String to) {
        AIResponse aiResponse = aiService.extractData(request);
        if (aiResponse == null)
            return ResponseEntity.badRequest().body("Failed to extract data from text.");

        // Persist for demo purposes
        extractionRepository.save(extractionMapper.toEntity(aiResponse));

        emailSenderService.sendAIExtractionByEmail(to, aiResponse);
        return ResponseEntity.ok("Sent AI extraction to email");
    }

    @PostMapping("/ai/slack")
    @io.swagger.v3.oas.annotations.Operation(summary = "Extract and send to Slack", description = "Extracts data from raw text using AI and sends it to Slack.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Extraction sent to Slack")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to extract data")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<String> sendAIExtractionToSlack(@RequestBody ExtractionRequest request) {
        AIResponse aiResponse = aiService.extractData(request);
        if (aiResponse == null)
            return ResponseEntity.badRequest().body("Failed to extract data from text.");

        // Persist for demo purposes
        extractionRepository.save(extractionMapper.toEntity(aiResponse));

        slackSenderService.sendAIExtractionToSlack(aiResponse);
        return ResponseEntity.ok("Sent AI extraction to Slack");
    }

    @PostMapping("/ai/extract")
    @io.swagger.v3.oas.annotations.Operation(summary = "Extract data using AI", description = "Returns the structured JSON data extracted from raw text without sending it anywhere.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Data extracted successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to extract data")
    public ResponseEntity<AIResponse> extractData(@RequestBody ExtractionRequest request) {
        AIResponse aiResponse = aiService.extractData(request);
        if (aiResponse == null)
            return ResponseEntity.badRequest().build();

        // Persist for demo purposes
        extractionRepository.save(extractionMapper.toEntity(aiResponse));

        return ResponseEntity.ok(aiResponse);
    }
}
