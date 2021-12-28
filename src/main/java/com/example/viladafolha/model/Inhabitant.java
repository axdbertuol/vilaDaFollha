package com.example.viladafolha.model;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Inhabitant {

    private UUID id;
    private String name;
    private String surname;
    private Integer age;
    private Double cost;

    public Inhabitant(String name, String surname, Integer age, Double cost) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.cost = cost;
    }

    public Inhabitant() {
        this.id = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inhabitant that = (Inhabitant) o;
        return id.equals(that.id) && name.equals(that.name) && surname.equals(that.surname) && age.equals(that.age);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
