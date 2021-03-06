package com.viladafolha.model.transport;


import com.viladafolha.model.Message;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageDTO implements Serializable {

    private final long serial = 1L;

    private final String[] ACCEPTABLE_MSGS = new String[]{"PRINT", "SYS_MESSAGE"};

    private String sender;
    private String target;
    private String type;
    private String message;
    private Integer retries;
    private LocalDateTime timestamp;

    public MessageDTO() {
        this.timestamp = LocalDateTime.now();
        this.retries = 0;
    }


    public MessageDTO(Message message) {
        this.timestamp = message.getTimestamp();
        this.target = message.getTarget();
        this.retries = message.getRetries();
        this.type = message.getType();
        this.message = message.getMessage();
        this.sender = message.getSender();
    }


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MessageDTO copy(MessageDTO messageDTO) {
        return new MessageDTO();
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "sender='" + sender + '\'' +
                ", target='" + target + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", retries=" + retries +
                ", timestamp=" + timestamp +
                '}';
    }
}
