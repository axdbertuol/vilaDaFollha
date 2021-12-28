package com.example.viladafolha.model;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String email;
    private String role;
    private String password;

    public User(String email) {
        this.email = email;
    }

    public User(String email, String password) {
        this.id = 1L;
        this.email = email;
        this.password = password;
        this.role = "User";
    }

    public User() {
        this.email = "";
        this.password = "";
    }
}
