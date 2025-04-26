package com.DevSprint.voluntrix_backend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendThankYouEmail(String to, String name, String orderId, Double amount) {
        String subject = "Thank You for Your Donation to Voluntrix!";
        String content = """
                <html>
                <body>
                    <h2>Thank You, %s!</h2>
                    <p>We appreciate your generous donation to <strong>Voluntrix</strong>.</p>
                    <p><strong>Receipt:</strong></p>
                    <ul>
                        <li><strong>Order ID:</strong> %s</li>
                        <li><strong>Amount:</strong> LKR %.2f</li>
                    </ul>
                    <p>This means a lot to us and the people we serve. ❤️</p>
                    <br>
                    <p>- Team Voluntrix</p>
                </body>
                </html>
                """.formatted(name, orderId, amount);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // enable HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
