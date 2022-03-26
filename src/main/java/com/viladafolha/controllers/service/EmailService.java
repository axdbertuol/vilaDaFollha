package com.viladafolha.controllers.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.Message;
import com.viladafolha.model.transport.MailDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final AmazonSimpleEmailService mailSender;
    private final String sourceEmail;

    public EmailService(AmazonSimpleEmailService mailSender, @Value("${aws.verified.email}") String sourceEmail) {
        this.mailSender = mailSender;
        this.sourceEmail = sourceEmail;
    }

    @Async
    public void sendEmail(MailDTO mail) throws MessagingException {
        SendEmailRequest message = getEmailRequest(mail);
        mailSender.sendEmail(message);
    }

    private SendEmailRequest getEmailRequest(MailDTO mail) throws MessagingException {

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(mail.getTo()))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withText(new Content()
                                        .withCharset("UTF-8").withData(mail.getText())))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(mail.getSubject())))
                .withSource(mail.getFrom());

        return request;
    }

    public String getSourceEmail() {
        return sourceEmail;
    }
}
