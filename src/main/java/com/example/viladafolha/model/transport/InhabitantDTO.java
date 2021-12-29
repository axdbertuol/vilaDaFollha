package com.example.viladafolha.model.transport;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Data
@EqualsAndHashCode
public class InhabitantDTO {
    private Long id;
    private String name;
    private String lastName;
    private String cpf;
    private String email;
    private String password;
    private Date birthday;
    private Double balance;
    private Set<String> roles;


    public InhabitantDTO(String name, String lastName, String cpf, String email,
                      String password, Date birthday, Double balance, Set<String> roles) {
        this.name = name;
        this.lastName = lastName;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.balance = balance;
        this.roles = roles;
    }

    public InhabitantDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}