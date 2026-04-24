package com.example.apibridge.service;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionResponse;
import com.example.apibridge.exception.NotificationException;
import com.example.apibridge.util.MessageFormatter;
import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SlackSenderService {
    private final String slackWebhookUrl;
    private final Slack slack;

    public SlackSenderService(@Value("${slack.webhook.url}") String slackWebhookUrl, Slack slack) {
        this.slackWebhookUrl = slackWebhookUrl;
        this.slack = slack;
    }

    public void sendExtractionToSlack(ExtractionResponse extraction) {
        try {
            String text = MessageFormatter.formatExtraction(extraction);
            Payload payload = Payload.builder().text(text).build();
            this.slack.send(slackWebhookUrl, payload);
        } catch (Exception e) {
            throw new NotificationException("Failed to send extraction to Slack", e);
        }
    }

    public void sendAIExtractionToSlack(AIResponse aiResponse) {
        try {
            String text = MessageFormatter.formatAIExtraction(aiResponse);
            Payload payload = Payload.builder().text(text).build();
            this.slack.send(slackWebhookUrl, payload);
        } catch (Exception e) {
            throw new NotificationException("Failed to send AI extraction to Slack", e);
        }
    }

}
