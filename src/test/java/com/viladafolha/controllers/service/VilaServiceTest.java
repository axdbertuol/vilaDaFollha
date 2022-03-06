package com.viladafolha.controllers.service;

import com.viladafolha.model.FinancialReport;
import com.viladafolha.model.Inhabitant;
import com.viladafolha.repos.InhabitantRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;


import java.util.List;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VilaServiceTest {

    @InjectMocks
    VilaService vilaService;
    @Mock
    InhabitantRepo inhabitantRepo;
    @Mock
    InhabitantService inhabitantService;


    @Test
    void givenListOfInhabitants_whenGetTotalCost_thenSucceed() {
        var inhab1 = new Inhabitant();
        inhab1.setBalance(103.0);
        var inhab2 = new Inhabitant();
        inhab2.setBalance(102.0);
        var inhab3 = new Inhabitant();
        inhab3.setBalance(101.0);

        when(inhabitantRepo.findAll()).thenReturn(List.of(inhab1, inhab2, inhab3));
        var result = vilaService.getTotalCost();

        var expected = inhab1.getBalance() + inhab2.getBalance() + inhab3.getBalance();
        verify(inhabitantRepo).findAll();
        assert result == expected;
    }

    @Test
    void givenListOfInhabitants_whenGetTotalBalance_thenSucceed() {
        var inhab1 = new Inhabitant();
        inhab1.setBalance(103.0);
        var inhab2 = new Inhabitant();
        inhab2.setBalance(102.0);
        var inhab3 = new Inhabitant();
        inhab3.setBalance(101.0);
        var totalCost = inhab1.getBalance() + inhab2.getBalance() + inhab3.getBalance();
        var vilaBudget2 = 307.0;
        ReflectionTestUtils.setField(vilaService, "vilaBudget", "307");

        when(inhabitantRepo.findAll()).thenReturn(List.of(inhab1, inhab2, inhab3));

        var result = vilaService.getTotalBalance();

        var expected = vilaBudget2 - totalCost;
        verify(inhabitantRepo).findAll();
        assert result == expected;
    }

    @Test
    void givenListOfInhabitants_whenGetFinancialReport_thenSucceed() {
        var inhab1 = new Inhabitant();
        inhab1.setBalance(103.0);
        var inhab2 = new Inhabitant();
        inhab2.setBalance(102.0);
        var inhab3 = new Inhabitant();
        inhab3.setBalance(101.0);
        var vilaBudget = 307.0;
        var totalCost = inhab1.getBalance() + inhab2.getBalance() + inhab3.getBalance();
        var totalBalance = vilaBudget - totalCost;
        ReflectionTestUtils.setField(vilaService, "vilaBudget", "307");

        when(inhabitantRepo.findAll()).thenReturn(List.of(inhab1, inhab2, inhab3));
        when(inhabitantService.getMostExpensiveInhabitant()).thenReturn(inhab1.toDTO());
        FinancialReport financialReport = vilaService.getFinancialReport();
        var expected = new FinancialReport(totalCost, vilaBudget, totalBalance, inhab1.toDTO());

        verify(inhabitantRepo, verificationData -> List.of(inhab1, inhab2, inhab3)).findAll();
        assert expected.equals(financialReport);

    }
}