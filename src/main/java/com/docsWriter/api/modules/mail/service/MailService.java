package com.docsWriter.api.modules.mail.service;

import com.docsWriter.api.enums.OtpPurpose;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp, OtpPurpose purpose, int ttlMinutes) {
        String subject = switch (purpose) {
            case RESET_PASSWORD -> "DocsWriter - Password reset code";
            case VERIFY_EMAIL -> "DocsWriter - Verify your email";
        };

        String body = """
                Your verification code is: %s
                
                This code is valid for %d minutes.
                If you did not request this, please ignore this email.
                """.formatted(otp, ttlMinutes);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

}
