package com.example.viladafolha.model;

import com.example.viladafolha.model.transport.InhabitantDTO;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "inhabitants")
public class Inhabitant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String lastName;
    @Column(unique = true)
    private String cpf;
    @Column(unique = true)
    private String email;
    private String password;
    private Date birthday;
    private Double balance;
    @ElementCollection
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

    public Inhabitant() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public InhabitantDTO toDTO() {
        return new InhabitantDTO(id, name, lastName, cpf, email, password, birthday, balance, roles);
    }
}
