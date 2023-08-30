package com.disi.travelpoints.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("disitravelpoints@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendMessageToAdmin(String sendFrom, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("disitravelpoints@gmail.com");
        message.setTo(sendFrom);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}