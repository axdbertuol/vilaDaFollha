package com.viladafolha.controllers.rest;


import com.viladafolha.controllers.service.MessageQueryService;
import com.viladafolha.controllers.service.UserService;
import com.viladafolha.model.transport.MessageDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MsgReportRest {


    private MessageQueryService messageQueryService;

    public MsgReportRest(MessageQueryService messageQueryService) {
        this.messageQueryService = messageQueryService;
    }

    @PostMapping(path = "/send",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> send(@RequestBody MessageDTO messageDTO) {
        var response = messageQueryService.processMessageQuery(messageDTO);
        return !response ?
                ResponseEntity.badRequest().body("Something went wrong.")
                : ResponseEntity.ok("Message sent successfully to queue.");
    }


}
