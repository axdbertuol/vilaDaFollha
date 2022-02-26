package com.example.viladafolha.model.transport;

import com.example.viladafolha.model.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
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
    private LocalDate birthday;
    private Double balance;
    private Set<Role> roles;


    public InhabitantDTO(Long id, String name, String lastName, String cpf, String email,
                      String password, LocalDate birthday, Double balance, Set<Role> roles) {
        this.id = id;
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

    public InhabitantDTO(){
        this.email = "";
        this.password = "";
    }
}
