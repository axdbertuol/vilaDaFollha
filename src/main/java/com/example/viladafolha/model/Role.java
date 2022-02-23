package com.example.viladafolha.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
public class Role {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<Inhabitant> inhabitants;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Privilege> privileges;

    public Role() {

    }

    public Role(String name, Collection<Inhabitant> inhabitants, Collection<Privilege> privileges) {
        this.name = name;
        this.inhabitants = inhabitants;
        this.privileges = privileges;
    }
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
        this.inhabitants = Collections.emptyList();
        this.privileges = Collections.emptyList();
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

    public Collection<Inhabitant> getInhabitants() {
        return inhabitants;
    }

    public void setInhabitants(Collection<Inhabitant> inhabitants) {
        this.inhabitants = inhabitants;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }
}
