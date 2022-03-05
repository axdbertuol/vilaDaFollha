package com.viladafolha.enums;

public enum MessageType {
    GENERATE_PDF_REPORT("Gerar relatório em pdf"),
    PRINT_SYS_MSG("Printar mensagem no console");
    public final String name;
    MessageType(String name) {
        this.name = name;
    }
}
