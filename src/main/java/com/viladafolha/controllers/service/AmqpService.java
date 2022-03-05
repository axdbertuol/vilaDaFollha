package com.viladafolha.controllers.service;

import com.viladafolha.model.Message;
import com.viladafolha.model.transport.MessageDTO;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AmqpService {

    private final RabbitTemplate queueSender;
    private final Map<String, String> typesToNameQueueMap = new HashMap<>();
    private final VilaService vilaService;
    private final String exchangeName;


    public AmqpService(
            RabbitTemplate queueSender,
            @Value("${queue.name.1}") String queueName1,
            @Value("${queue.name.2}") String queueName2,
            VilaService vilaService, @Value("${exchange.name}") String exchangeName

    ) {
        this.queueSender = queueSender;
        this.vilaService = vilaService;
        this.exchangeName = exchangeName;
        typesToNameQueueMap.put("PRINT_SYSMSG", queueName1);
        typesToNameQueueMap.put("GENERATE_PDF", queueName2);


    }

    public boolean processMessageQuery(MessageDTO messageDTO) {


        // create model Message
        var messageModel = new Message(messageDTO);

        var response = false;
        switch (messageModel.getType()) {
            case "GENERATE_PDF" -> {
                var routingKey = typesToNameQueueMap.get("GENERATE_PDF");
                messageModel.setTarget(routingKey);
                var financialReport = vilaService.getFinancialReport();
                messageModel.setMessage(financialReport.toJSON().toString());
                MessageDTO newMessage = new MessageDTO(messageModel);
                try {
                    queueSender.convertAndSend(exchangeName, routingKey, newMessage);
                } catch (AmqpException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
                response = true;
            }
            case "PRINT_SYSMSG" -> {
                var routingKey = typesToNameQueueMap.get("PRINT_SYSMSG");
                messageModel.setTarget(routingKey);
                MessageDTO newMessage = new MessageDTO(messageModel);
                queueSender.convertAndSend(exchangeName, routingKey, newMessage);
                response = true;
            }
        }

        return response;
    }


}
