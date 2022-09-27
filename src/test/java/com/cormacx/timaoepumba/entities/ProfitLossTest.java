package com.cormacx.timaoepumba.entities;


import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.HeldStock;
import com.cormacx.timaoepumba.entities.account.HeldStockStatus;
import com.cormacx.timaoepumba.entities.aggregate.ProfitLoss;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ProfitLossTest {

    private String randomUserUUID = UUID.randomUUID().toString();
    private boolean setup;
    private Account account;

    @BeforeEach
    public void setup() {
        if(!setup) {
            account = new Account(randomUserUUID, 78956.75D, true);

        }
    }

    @Test
    public void canProfitLossCalculateItselfAndProfit() {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date lastWeek = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));

        Order order = new Order(OrderType.SELL, 100, "VALE3", 68.77, randomUserUUID, new Date(), account);
        HeldStock heldStock = new HeldStock(200, "VALE3", 13196D, 65.98D, lastWeek, account, HeldStockStatus.HELD);

        ProfitLoss profitLoss = new ProfitLoss(heldStock, order);
        assertEquals(279D, profitLoss.getTotalValue());
        assertEquals(2.79D, profitLoss.getAverage());
    }

    @Test
    public void canProfitLossCalculateLoss() {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date lastWeek = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));

        Order order = new Order(OrderType.SELL, 100, "VALE3", 61.77, randomUserUUID, new Date(), account);
        HeldStock heldStock = new HeldStock(200, "VALE3", 13196D, 65.98D, lastWeek, account, HeldStockStatus.HELD);

        ProfitLoss profitLoss = new ProfitLoss(heldStock, order);
        assertEquals(-421D, profitLoss.getTotalValue());
    }

    @Test
    public void canIdentifyDayTrade() {
        long HOUR_IN_MS = 1000 * 60 * 60;
        Date anHourAgo = new Date(System.currentTimeMillis() - HOUR_IN_MS);

        Order order = new Order(OrderType.SELL, 100, "VALE3", 67.27, randomUserUUID, new Date(), account);
        HeldStock heldStock = new HeldStock(100, "VALE3", 6598D, 65.98D, anHourAgo, account, HeldStockStatus.HELD);

        ProfitLoss profitLoss = new ProfitLoss(heldStock, order);
        assertTrue(profitLoss.isDayTrade());
    }


}
