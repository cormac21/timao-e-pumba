package com.cormacx.timaoepumba.entities.aggregate;

import com.cormacx.timaoepumba.entities.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

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
