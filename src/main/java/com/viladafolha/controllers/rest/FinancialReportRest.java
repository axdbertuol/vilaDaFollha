package com.viladafolha.controllers.rest;

import com.viladafolha.controllers.service.InhabitantService;
import com.viladafolha.controllers.service.VilaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/village/financial")
public class FinancialReportRest {


    private final VilaService vilaService;


    public FinancialReportRest(VilaService vilaService, InhabitantService inhabitantService) {
        this.vilaService = vilaService;
    }

    @GetMapping()
    public String getFinancialReport() {
        var financialReport =  vilaService.getFinancialReport();
        return financialReport.toJSON().toString();
    }


}
