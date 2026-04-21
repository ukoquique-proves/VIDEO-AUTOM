package com.example.apibridge.controller;

import com.example.apibridge.exception.ResourceNotFoundException;
import com.example.apibridge.service.ExtractionFetchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SendControllerErrorHandlingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExtractionFetchService extractionFetchService;

    @Test
    public void whenExtractionNotFound_shouldReturn404Json() throws Exception {
        when(extractionFetchService.fetchExtractionById(999L))
                .thenThrow(new ResourceNotFoundException("Extraction not found with ID: 999"));

        mockMvc.perform(post("/api/send/email/999")
                .param("to", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Extraction not found with ID: 999"));
    }

    @Test
    public void whenEmptyTextForAI_shouldReturn400Json() throws Exception {
        String emptyJson = "{\"text\":\"\"}";

        mockMvc.perform(post("/api/send/ai/extract")
                .content(emptyJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Input text cannot be empty for AI extraction."));
    }
}
