package com.Mission.Shakti.serviceImpl;

import com.Mission.Shakti.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Override
    public void sendRegistrationEmail(String to, String username) {
        sendEmail(to, "Registration Successful",
                "Hello " + username + ", your registration is pending approval.");
    }

    @Override
    public void sendApprovalEmail(String to, String username) {
        sendEmail(to, "Account Approved",
                "Hello " + username + ", your account has been approved.");
    }

    @Override
    public void sendRejectionEmail(String to, String username) {
        sendEmail(to, "Account Rejected",
                "Hello " + username + ", your account has been rejected.");
    }

    @Override
    public void sendLoginEmail(String to, String username) {
        sendEmail(to, "Login Alert",
                "Hello " + username + ", you have logged in successfully.");
    }
}