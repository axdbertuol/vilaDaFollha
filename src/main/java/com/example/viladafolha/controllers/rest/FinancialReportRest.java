package com.example.viladafolha.controllers.rest;

import com.example.viladafolha.controllers.service.VilaService;
import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.FinancialReport;
import com.example.viladafolha.model.Inhabitant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/village/financial")
public class FinancialReportRest {

    @Autowired
    private VilaService vilaService;

    @GetMapping()
    public FinancialReport getMostExpensiveInhabitant() throws InhabitantNotFoundException {
        Double totalCost = vilaService.getTotalCost().orElseThrow(NullPointerException::new);
        Double budget = vilaService.getVilaBudget().orElseThrow(NullPointerException::new);
        Double totalBalance = vilaService.getTotalBalance().orElseThrow(NullPointerException::new);
        Inhabitant inhabitant = vilaService.getMostExpensiveInhabitant().orElseThrow(InhabitantNotFoundException::new);

        return new FinancialReport(totalCost, budget, totalBalance, inhabitant);

    }


}
