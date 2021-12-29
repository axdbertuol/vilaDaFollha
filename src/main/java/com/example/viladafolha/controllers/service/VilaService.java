package com.example.viladafolha.controllers.service;

import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.model.InhabitantDao;
import com.example.viladafolha.model.transport.InhabitantDTO;
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

    public List<InhabitantDTO> list() throws InhabitantNotFoundException {
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
        List<InhabitantDTO> list = inhabitantDao.getAll();
        return list.stream().map(InhabitantDTO::getBalance).reduce(Double::sum);
    }


    public InhabitantDTO getInhabitant(Long id) throws InhabitantNotFoundException {
        return inhabitantDao.getById(id).orElseThrow(InhabitantNotFoundException::new);
    }

    public List<InhabitantDTO> getInhabitantByName(String name) {
        return inhabitantDao.getAllByFilter(name);
    }

    public Optional<InhabitantDTO> getMostExpensiveInhabitant() {
        return inhabitantDao.getAll().stream().max(Comparator.comparing(InhabitantDTO::getBalance));
    }

    public Double getTotalBalance() {
        return getVilaBudget().get() - getTotalCost().get();
    }
}
