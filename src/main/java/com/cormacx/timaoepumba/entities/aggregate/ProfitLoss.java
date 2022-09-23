package com.cormacx.timaoepumba.entities.aggregate;

import com.cormacx.timaoepumba.entities.account.DepositWithdrawal;
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
    private DepositWithdrawal resultingDepositWithdrawal;

    @OneToOne
    private Order sellOrder;

    private long differenceInTime;

}
