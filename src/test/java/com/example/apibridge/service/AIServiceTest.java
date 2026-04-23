package com.example.apibridge.service;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AIServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private AIService aiService;

    private static final String API_URL = "http://mock-groq/v1/chat/completions";
    private static final String API_KEY  = "mock-key";
    private static final String MODEL    = "mock-model";

    @BeforeEach
    public void setUp() {
        aiService = new AIService(API_KEY, MODEL, API_URL, restTemplate);
    }

    // --- Input validation ---

    @Test
    public void testNullRequestThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () -> aiService.extractData(null));
        verifyNoInteractions(restTemplate);
    }

    @Test
    public void testNullTextThrowsIllegalArgument() {
        ExtractionRequest request = new ExtractionRequest();
        request.setText(null);
        assertThrows(IllegalArgumentException.class, () -> aiService.extractData(request));
        verifyNoInteractions(restTemplate);
    }

    @Test
    public void testBlankTextThrowsIllegalArgument() {
        ExtractionRequest request = new ExtractionRequest();
        request.setText("   ");
        assertThrows(IllegalArgumentException.class, () -> aiService.extractData(request));
        verifyNoInteractions(restTemplate);
    }

    // --- Happy path ---

    @Test
    public void testSuccessfulExtraction() {
        String mockResponse = groqResponse("{\"companyName\":\"ACME Corp\",\"date\":\"2023-01-01\",\"totalAmount\":123.45}");
        when(restTemplate.postForObject(eq(API_URL), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        AIResponse result = aiService.extractData(requestWith("Invoice from ACME Corp for $123.45 on 2023-01-01"));

        assertNotNull(result);
        assertEquals("ACME Corp", result.getCompanyName());
        assertEquals("2023-01-01", result.getDate());
        assertEquals(123.45, result.getTotalAmount());
        verify(restTemplate, times(1)).postForObject(eq(API_URL), any(HttpEntity.class), eq(String.class));
    }

    @Test
    public void testNullFieldsInResponse() {
        String mockResponse = groqResponse("{\"companyName\":null,\"date\":null,\"totalAmount\":null}");
        when(restTemplate.postForObject(eq(API_URL), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        AIResponse result = aiService.extractData(requestWith("Some text with no extractable fields"));

        assertNotNull(result);
        assertNull(result.getCompanyName());
        assertNull(result.getDate());
        assertNull(result.getTotalAmount());
    }

    // --- Markdown fence stripping ---

    @Test
    public void testStripsJsonMarkdownFence() {
        String fencedContent = "```json\n{\"companyName\":\"Fenced Corp\",\"date\":\"2024-06-01\",\"totalAmount\":500.0}\n```";
        String mockResponse = groqResponseRaw(fencedContent);
        when(restTemplate.postForObject(eq(API_URL), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        AIResponse result = aiService.extractData(requestWith("Invoice from Fenced Corp"));

        assertEquals("Fenced Corp", result.getCompanyName());
        assertEquals(500.0, result.getTotalAmount());
    }

    @Test
    public void testStripsPlainMarkdownFence() {
        String fencedContent = "```\n{\"companyName\":\"Plain Corp\",\"date\":\"2024-07-01\",\"totalAmount\":99.9}\n```";
        String mockResponse = groqResponseRaw(fencedContent);
        when(restTemplate.postForObject(eq(API_URL), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        AIResponse result = aiService.extractData(requestWith("Invoice from Plain Corp"));

        assertEquals("Plain Corp", result.getCompanyName());
    }

    // --- Error handling ---

    @Test
    public void testApiCallFailureThrowsRuntime() {
        when(restTemplate.postForObject(eq(API_URL), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RestClientException("Connection refused"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> aiService.extractData(requestWith("Some invoice text")));
        assertTrue(ex.getMessage().contains("AI Extraction failed"));
    }

    @Test
    public void testMalformedJsonResponseThrowsRuntime() {
        String badResponse = groqResponseRaw("not-valid-json-at-all");
        when(restTemplate.postForObject(eq(API_URL), any(HttpEntity.class), eq(String.class)))
                .thenReturn(badResponse);

        assertThrows(RuntimeException.class,
                () -> aiService.extractData(requestWith("Some invoice text")));
    }

    @Test
    public void testNullApiResponseThrowsRuntime() {
        when(restTemplate.postForObject(eq(API_URL), any(HttpEntity.class), eq(String.class)))
                .thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> aiService.extractData(requestWith("Some invoice text")));
    }

    // --- Helpers ---

    private ExtractionRequest requestWith(String text) {
        ExtractionRequest req = new ExtractionRequest();
        req.setText(text);
        return req;
    }

    /** Wraps a JSON string in the Groq API response envelope. */
    private String groqResponse(String json) {
        return "{\"choices\":[{\"message\":{\"content\":" + escapeJson(json) + "}}]}";
    }

    /** Wraps raw content (e.g. fenced blocks) in the Groq envelope. */
    private String groqResponseRaw(String rawContent) {
        return "{\"choices\":[{\"message\":{\"content\":" + escapeJson(rawContent) + "}}]}";
    }

    private String escapeJson(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }
}
