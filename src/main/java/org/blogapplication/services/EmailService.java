package org.blogapplication.services;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Service
public class EmailService {

    public final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSuccessfulEmail(String userEmail, String username) {
        try {
            log.debug("Attempting to send email to: {}", userEmail);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(userEmail);
            helper.setSubject("Java Mail with Attachment");

            try (var inputStream = Objects.requireNonNull(EmailService.class.getResourceAsStream("/templates/successfulEmailMessage.html"))) {
                String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                htmlContent = htmlContent.replace("{{user}}", username);
                helper.setText(htmlContent, true);
            }

            mailSender.send(message);
            log.info("Email sent successfully to: {}", userEmail);
        } catch (Exception e) {
            log.error("Error sending mail to {}: {}", userEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

