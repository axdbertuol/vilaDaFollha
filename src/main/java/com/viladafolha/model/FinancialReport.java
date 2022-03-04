package com.viladafolha.model;

import com.google.gson.JsonObject;
import com.viladafolha.model.transport.InhabitantDTO;
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

    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("total_balance", getTotalBalance());
        json.addProperty("total_cost", getTotalCost());
        json.addProperty("budget", getBudget());
        json.addProperty("most_exp_inhabitant_id", getMostExpensiveInhabitant().getId());
        return json;
    }

}
