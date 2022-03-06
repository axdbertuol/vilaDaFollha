package com.viladafolha.model.transport;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
public class MailDTO {
    private String from;
    private String to;
    private String subject;
    private String text = "";
}