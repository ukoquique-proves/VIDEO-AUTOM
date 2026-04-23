package com.example.apibridge.service;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionResponse;
import com.example.apibridge.exception.NotificationException;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SlackSenderServiceTest {

    private SlackSenderService slackSenderService;

    @Mock
    private Slack slack;

    private final String webhookUrl = "http://localhost/mock-webhook";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        slackSenderService = new SlackSenderService(webhookUrl, slack);
    }

    @Test
    public void testSendExtractionToSlackSuccess() throws IOException {
        ExtractionResponse extraction = new ExtractionResponse();
        extraction.setCompanyName("Test Corp");
        
        when(slack.send(eq(webhookUrl), any(Payload.class))).thenReturn(mock(WebhookResponse.class));

        slackSenderService.sendExtractionToSlack(extraction);

        verify(slack, times(1)).send(eq(webhookUrl), any(Payload.class));
    }

    @Test
    public void testSendAIExtractionToSlackSuccess() throws IOException {
        AIResponse aiResponse = new AIResponse();
        aiResponse.setCompanyName("AI Corp");

        when(slack.send(eq(webhookUrl), any(Payload.class))).thenReturn(mock(WebhookResponse.class));

        slackSenderService.sendAIExtractionToSlack(aiResponse);

        verify(slack, times(1)).send(eq(webhookUrl), any(Payload.class));
    }

    @Test
    public void testSendSlackFailureThrowsNotificationException() throws IOException {
        ExtractionResponse extraction = new ExtractionResponse();
        
        when(slack.send(anyString(), any(Payload.class))).thenThrow(new IOException("Connection failed"));

        assertThrows(NotificationException.class, () -> {
            slackSenderService.sendExtractionToSlack(extraction);
        });
    }
}
