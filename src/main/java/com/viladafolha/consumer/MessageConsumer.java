package com.viladafolha.consumer;

import com.viladafolha.model.transport.MessageDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class MessageConsumer {

    private final RabbitTemplate queueSender;
    private final int MAX_RETRIES = 3;

    public MessageConsumer(RabbitTemplate queueSender) {
        this.queueSender = queueSender;
    }

    @RabbitListener(queues = "com.sysmsg.viladafolha")
    public void receiveSysMsgRequest(MessageDTO in) {
        in.incrementRetry();
        if (in.isValid()) {
            System.out.println(in.getMessage());
        } else {
            System.out.println();
        }
    }

    @RabbitListener(queues = "com.print.viladafolha")
    public void receivePrintReportRequest(MessageDTO in) {
        in.incrementRetry();
        if (in.isValid()) {
            // print
            System.out.println(in.getMessage());
        } else if (in.getRetries() >= MAX_RETRIES) {
            // send to email
        } else {
            // send back to queue
            System.out.println();
        }
    }
}


