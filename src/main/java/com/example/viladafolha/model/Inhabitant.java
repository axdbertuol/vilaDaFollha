package com.example.viladafolha.model;

import com.example.viladafolha.model.transport.InhabitantDTO;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Set;


@Data
public class Inhabitant {

    private Long id;
    private String name;
    private String lastName;
    private String cpf;
    private String email;
    private String password;
    private Date birthday;
    private Double balance;
    private Set<String> roles;


    public Inhabitant(String name, String lastName, String cpf, String email,
                      String password, Date birthday, Double balance, Set<String> roles) {
        this.name = name;
        this.lastName = lastName;
        this.cpf = cpf;
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.birthday = birthday;
        this.balance = balance;
        this.roles = roles;
    }


    public Inhabitant(InhabitantDTO inhabDto) {
        this(
                inhabDto.getName(),
                inhabDto.getLastName(),
                inhabDto.getCpf(),
                inhabDto.getEmail(),
                inhabDto.getPassword(),
                inhabDto.getBirthday(),
                inhabDto.getBalance(),
                inhabDto.getRoles()
        );
        this.setId(inhabDto.getId());
    }
}
