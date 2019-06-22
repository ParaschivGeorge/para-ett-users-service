package com.paraett.usersservice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    private JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendAccountActivationMessage(String to, String code) {
        String subject = "Para-ETT Account Activation";
        String text = "Welcome to Para-ETT!\n\n To activate your account access the url: https://para-ett.web.app/activation" +
                "?email=" + to +
                "&code=" + code +
                "\n\nFor support contact us at: para.ett.app@gmail.com\n Thank you for using our application.";
        sendSimpleMessage(to, subject, text);
    }
}

