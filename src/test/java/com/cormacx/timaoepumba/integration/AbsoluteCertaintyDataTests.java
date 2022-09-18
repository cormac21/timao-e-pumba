package com.cormacx.timaoepumba.integration;

import com.cormacx.timaoepumba.data.MigrationStation;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
import com.cormacx.timaoepumba.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AbsoluteCertaintyDataTests {

    private final OrderService orderService;
    private final MigrationStation migrationStation;


    @Autowired
    public AbsoluteCertaintyDataTests(OrderService orderService, MigrationStation migrationStation) {
        this.orderService = orderService;
        this.migrationStation = migrationStation;
    }

    @Test
    public void createSomeBuyAndSellOrdersAndSeeIfTheLatestIsTrulyTheLatest() {
        List<Order> orders = orderService.getAllOrders();

        Order latestBuyOrder = orderService.getLatestBuyOrderFromUserForTicker(migrationStation.genericGuyId, "MGLU3");
        assertEquals(100, latestBuyOrder.getQuantity());
        assertEquals( 4.7D, latestBuyOrder.getUnitPrice());
        assertEquals(latestBuyOrder.getType(), OrderType.BUY);
    }

}
