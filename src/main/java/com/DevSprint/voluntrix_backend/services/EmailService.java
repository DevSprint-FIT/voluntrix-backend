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

    public void sendVerificationEmail(String to, String name, String otp) {
        try {
            System.out.println("Attempting to send verification email to: " + to);
            String subject = "Verify Your Email - Voluntrix";
            String content = loadVerificationTemplate(name, otp);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            System.out.println("Verification email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send verification email to " + to + ": " + e.getMessage());
            // e.printStackTrace();
            throw new RuntimeException("Failed to send verification email: " + e.getMessage(), e);
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

    private String loadVerificationTemplate(String name, String otp) throws IOException {
        try {
            ClassPathResource resource = new ClassPathResource("templates/VerificationEmail.html");
            String content = "";
            
            // Try to read from classpath
            if (resource.exists()) {
                content = new String(resource.getInputStream().readAllBytes());
            }

            content = content.replace("{{name}}", name != null ? name : "User");
            content = content.replace("{{otp}}", otp);

            return content;
        } catch (Exception e) {
            System.err.println("Error loading verification template: " + e.getMessage());
            // Return a simple fallback template
            return createSimpleVerificationTemplate(name, otp);
        }
    }


    private String createSimpleVerificationTemplate(String name, String otp) {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2>Email Verification - Voluntrix</h2>
                <p>Hello %s,</p>
                <p>Your verification code is: <strong style="font-size: 24px;">%s</strong></p>
                <p>This code expires in 10 minutes.</p>
                <p>Best regards,<br>Voluntrix Team</p>
            </body>
            </html>
            """, name != null ? name : "User", otp);
    }
}
