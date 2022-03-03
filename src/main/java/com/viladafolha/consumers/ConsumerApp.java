package com.viladafolha.consumers;

import com.viladafolha.model.Message;
import com.viladafolha.model.transport.MessageDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConsumerApp {
    private final int MAX_RETRIES = 3;

    private final Map<String, String> typesToNameQueueMap = new HashMap<>();
    private final RabbitTemplate queueSender;
    private final String exchangeName;

    public ConsumerApp(
            RabbitTemplate queueSender,
            @Value("${queue.name.1}") String queueName1,
            @Value("${queue.name.2}") String queueName2,
            @Value("${exchange.name}") String exchangeName
    ) {
        this.queueSender = queueSender;
        typesToNameQueueMap.put("PRINT_SYSMSG", queueName1);
        typesToNameQueueMap.put("GENERATE_PDF", queueName2);
        this.exchangeName = exchangeName;


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
        System.out.println("passei");
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

        if (messageModel.getRetries() >= MAX_RETRIES) {
            // TODO send to email
            System.out.println("Max retries reached. Sent to email instead.");
            return;
        }


        switch (type) {
            case "PRINT_SYSMSG" -> {
                System.out.println("NEW AMQP MESSAGE FROM: " + messageModel.getSender());
                System.out.println("---------------------------------------------");
                System.out.println(messageModel.getMessage());
                System.out.println("---------------------------------------------");

            }
            case "GENERATE_PDF" -> {
                // TODO print report
            }
            default -> {
                // in case there's no available type send back to queue

                // serialize again
                in = new MessageDTO(messageModel);
                //send back to queue
                queueSender.convertAndSend(exchangeName, queue, in);
            }
        }
    }


}
