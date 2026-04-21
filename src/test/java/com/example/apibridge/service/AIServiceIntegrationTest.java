package com.example.apibridge.service;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class AIServiceIntegrationTest {

    @Autowired
    private AIService aiService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testExtractData() {
        String mockResponse = "{\"choices\":[{\"message\":{\"content\":\"{\\\"companyName\\\":\\\"ACME Corp\\\",\\\"date\\\":\\\"2023-01-01\\\",\\\"totalAmount\\\":123.45}\"}}]}";

        mockServer.expect(requestTo("https://api.groq.com/openai/v1/chat/completions"))
                .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

        ExtractionRequest request = new ExtractionRequest();
        request.setText("Invoice from ACME Corp for $123.45 on 2023-01-01");

        AIResponse response = aiService.extractData(request);

        assertNotNull(response);
        assertEquals("ACME Corp", response.getCompanyName());
        assertEquals("2023-01-01", response.getDate());
        assertEquals(123.45, response.getTotalAmount());

        mockServer.verify();
    }
}
