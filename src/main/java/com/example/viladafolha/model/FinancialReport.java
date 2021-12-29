package com.example.viladafolha.model;

import com.example.viladafolha.model.transport.InhabitantDTO;
import lombok.Data;

@Data
public class FinancialReport {

    private Double totalCost;
    private Double budget;
    private Double totalBalance;
    private InhabitantDTO mostExpensiveInhabitant;

    public FinancialReport(Double totalCost, Double budget, Double totalBalance, InhabitantDTO mostExpensiveInhabitant) {
        this.totalCost = totalCost;
        this.budget = budget;
        this.totalBalance = totalBalance;
        this.mostExpensiveInhabitant = mostExpensiveInhabitant;
    }
}
