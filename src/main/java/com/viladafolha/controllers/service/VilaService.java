package com.viladafolha.controllers.service;

import com.viladafolha.model.FinancialReport;
import com.viladafolha.model.Inhabitant;
import com.viladafolha.repos.InhabitantRepo;
import com.viladafolha.model.transport.InhabitantDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VilaService {
    private String vilaBudget;


    private final InhabitantRepo inhabitantRepo;
    private final InhabitantService inhabitantService;

    public VilaService(
            @Value("${app.vilaBudget}") String vilaBudget,
            InhabitantRepo inhabitantRepo,
            InhabitantService inhabitantService) {
        this.vilaBudget = vilaBudget;
        this.inhabitantRepo = inhabitantRepo;
        this.inhabitantService = inhabitantService;
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

    public FinancialReport getFinancialReport() {
        Double totalCost = getTotalCost();
        Double budget = getVilaBudget();
        Double totalBalance = getTotalBalance();
        InhabitantDTO inhabitant = inhabitantService.getMostExpensiveInhabitant();
        return new FinancialReport(totalCost, budget, totalBalance, inhabitant);
    }

}
