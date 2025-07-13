package com.DevSprint.voluntrix_backend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmailService(String to, String name, String orderId, Double amount) throws MailException, MessagingException,IOException{
        String subject = "Thank You for Your Donation to Voluntrix!";
        String content = loadTemplate(name, orderId, amount);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true); 

        mailSender.send(message);
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
