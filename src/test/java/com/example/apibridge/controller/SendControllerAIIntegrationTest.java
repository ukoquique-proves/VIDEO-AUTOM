package com.example.apibridge.controller;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionRequest;
import com.example.apibridge.service.AIService;
import com.example.apibridge.service.EmailSenderService;
import com.example.apibridge.service.ExtractionFetchService;
import com.example.apibridge.service.SlackSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SendController.class)
public class SendControllerAIIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AIService aiService;

    @MockBean
    private EmailSenderService emailSenderService;

    @MockBean
    private SlackSenderService slackSenderService;

    @MockBean
    private ExtractionFetchService extractionFetchService;

    @MockBean
    private com.example.apibridge.repository.ExtractionRepository extractionRepository;

    @MockBean
    private com.example.apibridge.mapper.ExtractionMapper extractionMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSendAIExtractionToEmail() throws Exception {
        ExtractionRequest request = new ExtractionRequest();
        request.setText("Invoice from ACME Corp for $123.45 on 2023-01-01");

        AIResponse aiResponse = new AIResponse();
        aiResponse.setCompanyName("ACME Corp");
        aiResponse.setDate("2023-01-01");
        aiResponse.setTotalAmount(123.45);

        when(aiService.extractData(any(ExtractionRequest.class))).thenReturn(aiResponse);
        doNothing().when(emailSenderService).sendAIExtractionByEmail(any(String.class), any(AIResponse.class));

        mockMvc.perform(post("/api/send/ai/email")
                .param("to", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSendAIExtractionToSlack() throws Exception {
        ExtractionRequest request = new ExtractionRequest();
        request.setText("Invoice from ACME Corp for $123.45 on 2023-01-01");

        AIResponse aiResponse = new AIResponse();
        aiResponse.setCompanyName("ACME Corp");
        aiResponse.setDate("2023-01-01");
        aiResponse.setTotalAmount(123.45);

        when(aiService.extractData(any(ExtractionRequest.class))).thenReturn(aiResponse);
        doNothing().when(slackSenderService).sendAIExtractionToSlack(any(AIResponse.class));

        mockMvc.perform(post("/api/send/ai/slack")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
