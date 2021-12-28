package com.example.viladafolha.controllers.service;

import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.model.InhabitantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VilaService {
    @Value("${app.vilaBudget}")
    private String vilaBudget;
    @Autowired
    private InhabitantDao inhabitantDao;

    public List<Inhabitant> list() throws InhabitantNotFoundException {
        if (inhabitantDao.getAll().isEmpty()) {
            throw new InhabitantNotFoundException();
        }
        return inhabitantDao.getAll();
    }

    public Optional<Double> getVilaBudget() {
        return Optional.of(Double.parseDouble(vilaBudget));
    }

    public void setVilaBudget(Double vilaBudget) {
        this.vilaBudget = String.valueOf(vilaBudget);
    }

    public Optional<Double> getTotalCost() {
        List<Inhabitant> list = inhabitantDao.getAll();
        return list.stream().map(Inhabitant::getCost).reduce(Double::sum);
    }


    public Inhabitant getInhabitant(UUID id) throws InhabitantNotFoundException {
        return inhabitantDao.get(id).orElseThrow(InhabitantNotFoundException::new);
    }

    public List<Inhabitant> getInhabitantByName(String name) {
        return inhabitantDao.getByName(name);
    }

    public Optional<Inhabitant> getMostExpensiveInhabitant() {
        return inhabitantDao.getAll().stream().max(Comparator.comparing(Inhabitant::getCost));
    }

     public Optional<Double> getTotalBalance() {
        return Optional.of(getVilaBudget().get() - getTotalCost().get());
    }
}
