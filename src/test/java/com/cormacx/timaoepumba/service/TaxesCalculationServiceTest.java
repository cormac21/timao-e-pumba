package com.cormacx.timaoepumba.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaxesCalculationServiceTest {

    @InjectMocks
    private TaxesCalculationService taxesCalculationService;

    @Mock
    private OrderService orderService;

    @Mock
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        //when(orderService.getLatestOrderForStock());
    }

    @Test
    public void canPairUpDepositWithdrawalWithSellOrder() {
        //PairOfOrderAndDepositWithdrawal pair = taxesCalculationService.getPairOfOrderAndDepositWithdrawal();


    }


}
