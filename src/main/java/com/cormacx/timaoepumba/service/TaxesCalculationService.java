package com.cormacx.timaoepumba.service;

import org.springframework.stereotype.Service;

@Service
public class TaxesCalculationService {

    private OrderService orderService;

    private AccountService accountService;

    public TaxesCalculationService(OrderService orderService, AccountService accountService) {
        this.orderService = orderService;
        this.accountService = accountService;
    }



}
