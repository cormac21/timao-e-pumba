package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.HeldStock;
import com.cormacx.timaoepumba.entities.aggregate.ProfitLoss;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.repositories.ProfitLossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfitLossService {

    private final ProfitLossRepository profitLossRepository;

    @Autowired
    public ProfitLossService(ProfitLossRepository profitLossRepository) {
        this.profitLossRepository = profitLossRepository;
    }


    public ProfitLoss registerProfitLoss(HeldStock accountHeldStock, Order order) {
        ProfitLoss profitLoss = new ProfitLoss();
        profitLoss.setSellOrder(order);
        profitLoss.setOriginatingStockSold(accountHeldStock);
        profitLoss.setTotalValue(accountHeldStock.getTotalPrice() - order.getTotalPrice());
        double totalAmount = accountHeldStock.getTotalPrice() - order.getTotalPrice();
        Integer quantity = accountHeldStock.getQuantity() - order.getQuantity();
        profitLoss.setAverage(totalAmount/quantity);

        long differenceInTime = order.getCreatedOn().getTime() - accountHeldStock.getLastAcquired().getTime();
        profitLoss.setDifferenceInTime(differenceInTime);

        return profitLoss;
    }

}
