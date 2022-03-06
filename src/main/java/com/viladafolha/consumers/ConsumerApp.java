package com.viladafolha.consumers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lowagie.text.DocumentException;
import com.viladafolha.controllers.service.EmailService;
import com.viladafolha.enums.MessageType;
import com.viladafolha.model.Message;
import com.viladafolha.model.transport.MailDTO;
import com.viladafolha.model.transport.MessageDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConsumerApp {
    private static final String HTML_INPUT = "src/main/resources/templates/FinancialReport.html";
    private static final String PDF_OUTPUT = "src/main/resources/FinancialReport.pdf";
    private final int MAX_RETRIES = 3;

    private final RabbitTemplate queueSender;
    private final EmailService emailService;

    private final Map<String, String> typesToNameQueueMap = new HashMap<>();
    private final String exchangeName;
    private final String sourceEmail;


    public ConsumerApp(
            RabbitTemplate queueSender,
            @Value("${exchange.name}") String exchangeName,
            @Value("${queue.name.1}") String queueName1,
            @Value("${queue.name.2}") String queueName2,
            EmailService emailService,
            @Value("${aws.verified.email}") String sourceEmail) {
        this.queueSender = queueSender;
        this.exchangeName = exchangeName;
        this.emailService = emailService;

        this.sourceEmail = sourceEmail;
        typesToNameQueueMap.put("PRINT_SYS_MSG", queueName1);
        typesToNameQueueMap.put("GENERATE_PDF_REPORT", queueName2);


    }

    @RabbitListener(queues = "queue.print.viladafolha")
    public void receiveSysMsgRequest(MessageDTO in) {
        receive(in);
    }

    @RabbitListener(queues = "queue.pdf.viladafolha")
    public void receivePrintReportRequest(MessageDTO in) {
        receive(in);
    }


    private void receive(MessageDTO in) {
        Message messageModel = new Message(in);
        messageModel.incrementRetry();

        String type = messageModel.getType();
        String queue = "";

        try {
            queue = typesToNameQueueMap.get(type);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Invalid amqp message type. " + type);
            return;
        }

        if (!messageModel.isValid()) {
            System.out.println("Message is invalid.");
            return;
        }


        switch (type) {
            case "PRINT_SYS_MSG" -> {
                System.out.println("---------------------------------------------");
                System.out.println("NEW AMQP MESSAGE FROM: " + messageModel.getSender());
                System.out.println("---------------------------------------------");
                System.out.println(messageModel.getMessage());
                System.out.println("---------------------------------------------");
                System.out.println("---------------------------------------------");

            }
            case "GENERATE_PDF_REPORT" -> {

                try {
                    File inputHTML = new File(HTML_INPUT);
                    Document html = mapHtmlIdsToValues(inputHTML, messageModel);

                    if (messageModel.getRetries() >= MAX_RETRIES) {
                        MailDTO mailDTO = new MailDTO();
                        mailDTO.setFrom(sourceEmail);
                        mailDTO.setTo(messageModel.getSender());
                        mailDTO.setText(html.html());
                        mailDTO.setSubject("You have reached max retries to a pdf request.");

                        emailService.sendEmail(mailDTO);
                        return;
                    }
                    File outputPdf = new File(PDF_OUTPUT);

                    int n = 1;
                    while (outputPdf.exists()) {
                        outputPdf = new File(PDF_OUTPUT.replaceFirst(".pdf", "(" + n + ").pdf"));
                        n++;
                    }
                    xhtmlToPdf(html, outputPdf);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                    System.err.println("Something went wrong while sending email");
                }

            }
            default -> {
                // in case there's no type
                // serialize again
                in = new MessageDTO(messageModel);
                // send back to queue
                queueSender.convertAndSend(exchangeName, queue, in);
            }
        }
    }

    private void xhtmlToPdf(Document inputHtml, File outputPdf) {

        try (OutputStream outputStream = new FileOutputStream(outputPdf)) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            renderer.setDocumentFromString(inputHtml.html());
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private Document mapHtmlIdsToValues(File inputHTML, Message messageModel) throws IOException {
        Document document = Jsoup.parse(inputHTML, "UTF-8");

        JsonObject json = (JsonObject) JsonParser.parseString(messageModel.getMessage());
        try {
            document.getElementById("total-balance-value").text(String.valueOf(json.get("total_balance")));
            document.getElementById("total-cost-value").text(String.valueOf(json.get("total_cost")));
            document.getElementById("budget-value").text(String.valueOf(json.get("budget")));
            document.getElementById("most-exp-value").text(String.valueOf(json.get("most_exp_inhabitant_id")));
            document.getElementById("sender").text(messageModel.getSender());
            document.getElementById("target").text(messageModel.getTarget());
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        return document;
    }


}
