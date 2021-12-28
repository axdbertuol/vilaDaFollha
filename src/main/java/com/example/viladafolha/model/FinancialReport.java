package com.example.viladafolha.model;

import lombok.Data;

@Data
public class FinancialReport {

    private Double totalCost;
    private Double budget;
    private Double totalBalance;
    private Inhabitant mostExpensiveInhabitant;

    public FinancialReport(Double totalCost, Double budget, Double totalBalance, Inhabitant mostExpensiveInhabitant) {
        this.totalCost = totalCost;
        this.budget = budget;
        this.totalBalance = totalBalance;
        this.mostExpensiveInhabitant = mostExpensiveInhabitant;
    }
}
