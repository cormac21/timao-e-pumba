package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.aggregate.ProfitLoss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TaxesCalculationService {

    private final OrderService orderService;

    private final AccountService accountService;

    private final ProfitLossService profitLossService;

    @Autowired
    public TaxesCalculationService(OrderService orderService, AccountService accountService,
                                   ProfitLossService profitLossService) {
        this.orderService = orderService;
        this.accountService = accountService;
        this.profitLossService = profitLossService;
    }

    public BigDecimal taxesDueForMonth(long accountId, int year, int month) {
        List<ProfitLoss> profitsAndLossesOnTheMonth = profitLossService.getProfitLossesForMonth(accountId, year, month);


        return BigDecimal.valueOf(0.0);
    }


}
