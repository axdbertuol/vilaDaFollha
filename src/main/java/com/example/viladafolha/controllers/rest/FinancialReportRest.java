package com.example.viladafolha.controllers.rest;

import com.example.viladafolha.controllers.service.UserService;
import com.example.viladafolha.controllers.service.VilaService;
import com.example.viladafolha.exceptions.InhabitantNotFoundException;
import com.example.viladafolha.model.FinancialReport;
import com.example.viladafolha.model.transport.InhabitantDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/village/financial")
public class FinancialReportRest {

    private final VilaService vilaService;
    private final UserService userService;

    public FinancialReportRest(VilaService vilaService, UserService userService) {
        this.vilaService = vilaService;
        this.userService = userService;
    }

    @GetMapping()
    public FinancialReport getMostExpensiveInhabitant() {
        Double totalCost = vilaService.getTotalCost().orElseThrow(RuntimeException::new);
        Double budget = vilaService.getVilaBudget().orElseThrow(RuntimeException::new);
        Double totalBalance = vilaService.getTotalBalance();
        InhabitantDTO inhabitant = userService.getMostExpensiveInhabitant().orElseThrow(RuntimeException::new);

        return new FinancialReport(totalCost, budget, totalBalance, inhabitant);

    }


}
