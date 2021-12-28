package com.example.viladafolha.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InhabitantDao implements Dao<Inhabitant> {
    private Map<UUID, Inhabitant> inhabitants = new HashMap();

    @Autowired
    public InhabitantDao() {
        Inhabitant in = new Inhabitant("Alex", "Bert", 30, 500.0);
        inhabitants.put(in.getId(), in);
        in = new Inhabitant("Jao", "Silva", 21, 1500.0);
        inhabitants.put(in.getId(), in);
        in = new Inhabitant("Jao", "Xis", 24, 3000.0);
        inhabitants.put(in.getId(), in);
    }

    @Override
    public Optional<Inhabitant> get(UUID id) {
        return Optional.ofNullable(inhabitants.get(id));
    }

    @Override
    public List<Inhabitant> getAll() {
        return inhabitants.values().stream().toList();
    }

    @Override
    public void save(Inhabitant inhabitant) {
        inhabitants.put(inhabitant.getId(), inhabitant);
    }

    @Override
    public void update(Inhabitant inhabitant, String[] params) {
        inhabitant.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
        inhabitant.setSurname(Objects.requireNonNull(params[1], "Surname cannot be null"));
        inhabitant.setAge(Integer.parseInt(params[2]));
        inhabitant.setCost(Double.parseDouble(params[3]));
        inhabitants.put(inhabitant.getId(), inhabitant);
    }

    @Override
    public void delete(Inhabitant inhabitant) {
        inhabitants.remove(inhabitant.getId());
    }

    public List<Inhabitant> getByName(String name) {
        List<Inhabitant> result = inhabitants.values()
                .stream()
                .filter(inhabitant -> inhabitant.getName().equalsIgnoreCase(name))
                .toList();

        System.out.println("aaaa + " + result);

        return result;
    }
}
