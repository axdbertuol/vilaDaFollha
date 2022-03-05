package com.viladafolha.consumers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lowagie.text.DocumentException;
import com.viladafolha.model.Message;
import com.viladafolha.model.transport.MessageDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

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

    private final Map<String, String> typesToNameQueueMap = new HashMap<>();
    private final RabbitTemplate queueSender;
    private final String exchangeName;


    public ConsumerApp(
            RabbitTemplate queueSender,
            @Value("${exchange.name}") String exchangeName,
            @Value("${queue.name.1}") String queueName1,
            @Value("${queue.name.2}") String queueName2
    ) {
        this.queueSender = queueSender;
        this.exchangeName = exchangeName;
        typesToNameQueueMap.put("PRINT_SYSMSG", queueName1);
        typesToNameQueueMap.put("GENERATE_PDF", queueName2);


    }

    @RabbitListener(queues = "queue.printsysmsg.viladafolha")
    public void receiveSysMsgRequest(MessageDTO in) {
        receive(in, "PRINT_SYSMSG");
    }

    @RabbitListener(queues = "queue.generatepdf.viladafolha")
    public void receivePrintReportRequest(MessageDTO in) {
        receive(in, "GENERATE_PDF");
    }


    private void receive(MessageDTO in, String type) {
        Message messageModel = new Message(in);
        messageModel.incrementRetry();

        String queue = "";

        try {
            queue = typesToNameQueueMap.get(type);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (!messageModel.isValid()) {
            System.out.println("Message is invalid.");
            return;
        }


        switch (type) {
            case "PRINT_SYSMSG" -> {
                System.out.println("---------------------------------------------");
                System.out.println("NEW AMQP MESSAGE FROM: " + messageModel.getSender());
                System.out.println("---------------------------------------------");
                System.out.println(messageModel.getMessage());
                System.out.println("---------------------------------------------");
                System.out.println("---------------------------------------------");

            }
            case "GENERATE_PDF" -> {
                if (messageModel.getRetries() >= MAX_RETRIES) {
                    // TODO send pdf to email
                    System.out.println("Max retries reached. Sent to email instead.");
                    return;
                }
                try {
                    File inputHTML = new File(HTML_INPUT);
                    Document html = mapHtmlIdsToValues(inputHTML, messageModel);
                    File outputPdf = new File(PDF_OUTPUT);
                    int n = 1;
                    while (outputPdf.exists()) {
                        outputPdf = new File(PDF_OUTPUT.replaceFirst(".pdf", "(" + n + ").pdf"));
                        n++;
                    }
                    xhtmlToPdf(html, outputPdf);
                } catch (IOException e) {
                    e.printStackTrace();
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
        document.getElementById("total-balance-value").text(String.valueOf(json.get("total_balance")));
        document.getElementById("total-cost-value").text(String.valueOf(json.get("total_cost")));
        document.getElementById("budget-value").text(String.valueOf(json.get("budget")));
        document.getElementById("most-exp-value").text(String.valueOf(json.get("most_exp_inhabitant_id")));
        document.getElementById("sender").text(messageModel.getSender());
        document.getElementById("target").text(messageModel.getTarget());
        document.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml);

        return document;
    }


}
