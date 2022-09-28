package com.cormacx.timaoepumba.entities.aggregate;

import com.cormacx.timaoepumba.entities.order.Order;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity
public class ProfitLoss {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    private double totalValue;

    private double average;

    private int amountSold;

    private double stockAveragePrice;

    private double stockTotalPrice;

    private long accountId;

    @OneToOne
    private Order sellOrder;

    private long differenceInTime;

    private boolean isDayTrade;

    public ProfitLoss(Double stockAveragePrice, Double stockTotalPrice, Date lastAcquired, Long accountId, Order sellOrder) {
        this.sellOrder = sellOrder;
        this.amountSold = sellOrder.getQuantity();
        this.stockAveragePrice = stockAveragePrice;
        this.accountId = accountId;
        this.stockTotalPrice = stockTotalPrice;
        this.totalValue = sellOrder.getTotalPrice() - (stockAveragePrice * amountSold);
        this.average = totalValue / amountSold;
        this.differenceInTime = (lastAcquired.getTime() - sellOrder.getCreatedOn().getTime());
        if(differenceInTime <= 1000 * 60 * 60 * 24) {
            this.isDayTrade = true;
        }
    }

    protected ProfitLoss() {
    }
}
