package com.DevSprint.voluntrix_backend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public boolean sendEmailService(String to, String name, String orderId, Double amount) {
        String subject = "Thank You for Your Donation to Voluntrix!";

        try {
            String content = loadTemplate(name, orderId, amount);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); 

            mailSender.send(message);

            return true;
        } catch (MailException | MessagingException |IOException e) {
            // e.printStackTrace();
            System.out.println("I am here");
            return false;
        }
    }

    private String loadTemplate(String name, String orderId, Double amount) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/MailBody.html");
        Path path = resource.getFile().toPath();
        String content = Files.readString(path);

        content = content.replace("{{name}}", name);
        content = content.replace("{{orderId}}", orderId);
        content = content.replace("{{amount}}", String.format("%.2f", amount));

        return content;
    }
}
