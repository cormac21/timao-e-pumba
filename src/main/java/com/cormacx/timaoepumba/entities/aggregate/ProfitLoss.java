package com.cormacx.timaoepumba.entities.aggregate;

import com.cormacx.timaoepumba.entities.account.HeldStock;
import com.cormacx.timaoepumba.entities.order.Order;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigInteger;

@Data
@Entity
public class ProfitLoss {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    private double totalValue;

    private double average;

    @OneToOne
    private HeldStock originatingStockSold;

    @OneToOne
    private Order sellOrder;

    private long differenceInTime;

    private boolean isDayTrade;

    public ProfitLoss(HeldStock originatingStock, Order sellOrder) {
        this.originatingStockSold = originatingStock;
        this.sellOrder = sellOrder;
        int quantity = originatingStock.getQuantity() - sellOrder.getQuantity();
        Double boughtMatch = quantity * originatingStock.getAveragePrice();
        this.totalValue = sellOrder.getTotalPrice() - boughtMatch;
        this.average = totalValue / quantity;
        this.differenceInTime = (originatingStock.getLastAcquired().getTime() - sellOrder.getCreatedOn().getTime());
        if(differenceInTime <= 1000 * 60 * 60 * 24) {
            this.isDayTrade = true;
        }
    }

    protected ProfitLoss() {
    }
}
