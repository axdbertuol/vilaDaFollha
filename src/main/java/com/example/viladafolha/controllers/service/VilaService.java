package com.example.viladafolha.controllers.service;

import com.example.viladafolha.model.InhabitantDao;
import com.example.viladafolha.model.transport.InhabitantDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VilaService {
    @Value("${app.vilaBudget}")
    private String vilaBudget;


    private final InhabitantDao inhabitantDao;

    public VilaService(InhabitantDao inhabitantDao) {
        this.inhabitantDao = inhabitantDao;
    }

    public List<InhabitantDTO> list(){
        return inhabitantDao.getAll();
    }

    public Optional<Double> getVilaBudget() {
        return Optional.of(Double.parseDouble(vilaBudget));
    }

    public Optional<Double> getTotalCost() {
        List<InhabitantDTO> list = inhabitantDao.getAll();
        return list.stream().map(InhabitantDTO::getBalance).reduce(Double::sum);
    }


    public Double getTotalBalance() {
        return getVilaBudget().orElse(0.0) - getTotalCost().orElse(0.0);
    }

}
