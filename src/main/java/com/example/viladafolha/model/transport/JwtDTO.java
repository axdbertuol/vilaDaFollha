package com.example.viladafolha.model.transport;

import lombok.Data;

@Data
public class JwtDTO {

    private String type;
    private String token;
    private long expiration;

    public JwtDTO(String type, String token, long expiration) {
        this.type = type;
        this.token = token;
        this.expiration = expiration;
    }

    public String getFullToken() {
        return getType() + " " + getToken();
    }
}