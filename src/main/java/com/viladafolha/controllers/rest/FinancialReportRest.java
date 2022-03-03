package com.viladafolha.controllers.rest;

import com.viladafolha.controllers.service.UserService;
import com.viladafolha.controllers.service.VilaService;
import com.viladafolha.model.FinancialReport;
import com.viladafolha.model.transport.InhabitantDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
@RequestMapping("/village/financial")
public class FinancialReportRest implements Serializable {

    private final long serial = 1L;

    private final VilaService vilaService;
    private final UserService userService;

    public FinancialReportRest(VilaService vilaService, UserService userService) {
        this.vilaService = vilaService;
        this.userService = userService;
    }

    @GetMapping()
    public FinancialReport getFinancialReport() {
        Double totalCost = vilaService.getTotalCost();
        Double budget = vilaService.getVilaBudget();
        Double totalBalance = vilaService.getTotalBalance();
        InhabitantDTO inhabitant = userService.getMostExpensiveInhabitant();
        return new FinancialReport(totalCost, budget, totalBalance, inhabitant);
    }


}
