package com.viladafolha.controllers.service;

import com.viladafolha.model.transport.MailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String sourceEmail;

    public EmailService(JavaMailSender mailSender, @Value("${aws.verified.email}")  String sourceEmail) {
        this.mailSender = mailSender;
        this.sourceEmail = sourceEmail;
    }

    @Async
    public void sendEmail(MailDTO mail) throws MessagingException {
        MimeMessage message = getMimeMessage(mail);
        mailSender.send(message);
    }

    private MimeMessage getMimeMessage(MailDTO mail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getTo()));
        message.setContent(mail.getText(), "text/html");
        message.setFrom(new InternetAddress(mail.getFrom()));
        message.setSubject(mail.getSubject());

        return message;
    }

    public String getSourceEmail() {
        return sourceEmail;
    }
}
