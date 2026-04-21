package com.example.apibridge.service;

import com.example.apibridge.dto.AIResponse;
import com.example.apibridge.dto.ExtractionResponse;
import com.example.apibridge.util.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    private final JavaMailSender mailSender;
    private final Environment env;

    @Autowired
    public EmailSenderService(JavaMailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.env = env;
    }

    public void sendExtractionByEmail(String to, ExtractionResponse extraction) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Extraction Result");
        message.setText(MessageFormatter.formatExtraction(extraction));
        message.setFrom(env.getProperty("spring.mail.username", "noreply@example.com"));
        mailSender.send(message);
    }

    public void sendAIExtractionByEmail(String to, AIResponse aiResponse) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("AI Extraction Result");
        message.setText(MessageFormatter.formatAIExtraction(aiResponse));
        message.setFrom(env.getProperty("spring.mail.username", "noreply@example.com"));
        mailSender.send(message);
    }

}
