package com.viladafolha.controllers.rest;


import com.viladafolha.model.transport.MessageDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MsgReportRest {


    private final RabbitTemplate queueSender;

    public MsgReportRest(RabbitTemplate queueSender) {
        this.queueSender = queueSender;
    }


    @PostMapping(path = "/send",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> send(@RequestBody MessageDTO messageDTO) {

        switch (messageDTO.getType()){
            case "PRINT" -> messageDTO.setTarget("com.print.viladafolha");
            case "SYS_MSG" -> messageDTO.setTarget("com.sysmsg.viladafolha");
        }
        queueSender.convertAndSend("com.direct.viladafolha", messageDTO.getTarget(), messageDTO);
        return ResponseEntity.ok("Message has been sent.");
    }


}
