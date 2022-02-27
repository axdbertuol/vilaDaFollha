package com.example.viladafolha.controllers.service;

import com.example.viladafolha.model.Inhabitant;
import com.example.viladafolha.repos.InhabitantRepo;
import com.example.viladafolha.model.transport.InhabitantDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VilaService {
    @Value("${app.vilaBudget}")
    private String vilaBudget;


    private final InhabitantRepo inhabitantRepo;

    public VilaService(InhabitantRepo inhabitantRepo) {
        this.inhabitantRepo = inhabitantRepo;
    }

    public List<InhabitantDTO> list() {
        return inhabitantRepo.findAll().stream().map(Inhabitant::toDTO).toList();
    }

    public Double getVilaBudget() {
        return Double.parseDouble(vilaBudget);
    }

    public Double getTotalCost() {
        List<InhabitantDTO> list = inhabitantRepo.findAll().stream().map(Inhabitant::toDTO).toList();
        return list.stream().map(InhabitantDTO::getBalance).reduce(Double::sum).orElse(0.0);
    }


    public Double getTotalBalance() {
        return getVilaBudget() - getTotalCost();
    }

}
