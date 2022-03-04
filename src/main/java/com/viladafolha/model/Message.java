package com.viladafolha.model;


import com.viladafolha.model.transport.MessageDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Message implements Serializable {

    private final long serial = 1L;

    private final String[] ACCEPTABLE_MSGS = new String[]{"PRINT", "SYS_MESSAGE"};

    private String sender;
    private String target;
    private String type;
    private String message;

    private Integer retries;
    private LocalDateTime timestamp;

    public Message() {
        this.timestamp = LocalDateTime.now();
        this.retries = 0;
    }

    public Message(MessageDTO messageDTO) {
        this.timestamp = messageDTO.getTimestamp() == null ? LocalDateTime.now() : messageDTO.getTimestamp();
        this.retries = messageDTO.getRetries() == null ? 0 : messageDTO.getRetries();
        this.target = messageDTO.getTarget();
        this.type = messageDTO.getType();
        this.message = messageDTO.getMessage();
        this.sender = messageDTO.getSender();
    }


    public void incrementRetry() {
        retries++;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isValid() {
        // TODO
        // check if sender is valid
        // check if message is valid
        return true;
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

    public MessageDTO toDTO(MessageDTO messageDTO) {
        return new MessageDTO(this);
    }

    public Message copy(Message message) {
        return new Message();
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
