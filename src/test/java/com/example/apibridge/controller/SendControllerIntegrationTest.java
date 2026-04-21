package com.example.apibridge.controller;

import com.example.apibridge.dto.ExtractionResponse;
import com.example.apibridge.util.MessageFormatter;
import com.example.apibridge.model.Extraction;
import com.example.apibridge.repository.ExtractionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SendControllerIntegrationTest {
    // --- TEST-ONLY: Mock beans for email and Slack sending ---
    // These replace the real implementations ONLY during tests.
    // Production uses the real EmailSenderService and SlackSenderService.
    @org.springframework.boot.test.context.TestConfiguration
    static class MockConfig {
        @org.springframework.context.annotation.Bean
        public com.example.apibridge.service.EmailSenderService emailSenderService(
                org.springframework.mail.javamail.JavaMailSender mailSender,
                org.springframework.core.env.Environment env) {
            return new com.example.apibridge.service.EmailSenderService(mailSender, env) {
                @Override
                public void sendExtractionByEmail(String to, com.example.apibridge.dto.ExtractionResponse extraction) {
                    System.out.printf("[MOCK EMAIL] To: %s\\n%s\\n", to, MessageFormatter.formatExtraction(extraction));
                }
            };
        }

        @org.springframework.context.annotation.Bean
        public com.example.apibridge.service.SlackSenderService slackSenderService() {
            return new com.example.apibridge.service.SlackSenderService("mock-webhook-url") {
                @Override
                public void sendExtractionToSlack(com.example.apibridge.dto.ExtractionResponse extraction) {
                    System.out.printf("[MOCK SLACK] Message: %s\\n", MessageFormatter.formatExtraction(extraction));
                }
            };
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ExtractionRepository extractionRepository;

    private Long extractionId;

    @BeforeEach
    void setUp() {
        extractionRepository.deleteAll();
        Extraction extraction = new Extraction();
        extraction.setCompanyName("TestCorp");
        extraction.setDate("2024-01-01");
        extraction.setTotalAmount(1000.0);
        extractionId = extractionRepository.save(extraction).getId();
    }

    @Test
    void sendToEmail_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/send/email/" + extractionId)
                .param("to", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sendToSlack_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/send/slack/" + extractionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
