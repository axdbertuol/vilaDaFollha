package com.example.viladafolha.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;



@Data
public class Inhabitant {

    private Long id;
    private String name;
    private String lastName;
    private String cpf;
    private String email;
    private String password;
    private LocalDateTime birthday;
    private Double balance;
    private Set<String> roles;


    public Inhabitant(String name, String lastName, String cpf, String email,
                      String password, LocalDateTime birthday, Double balance, Set<String> roles) {
        this.name = name;
        this.lastName = lastName;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.balance = balance;
        this.roles = roles;
    }

    public Inhabitant(String email, String password){
        this.email = email;
        this.password = password;
    }
}
