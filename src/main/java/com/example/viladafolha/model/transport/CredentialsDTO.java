package com.example.viladafolha.model.transport;

import lombok.Data;

@Data
public class CredentialsDTO {

    private String email;
    private String password;

    public CredentialsDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
