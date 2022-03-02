package com.viladafolha.model;

import javax.persistence.*;

import java.util.Collection;
import java.util.Collections;

@Entity
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege(Long id, String name, Collection<Role> roles) {
        this.id = id;
        this.name = name;
        this.roles = roles;
    }

    public Privilege(Long id, String name) {
        this.id = id;
        this.name = name;
        this.roles = Collections.emptyList();
    }

    public Privilege() {

    }
}
