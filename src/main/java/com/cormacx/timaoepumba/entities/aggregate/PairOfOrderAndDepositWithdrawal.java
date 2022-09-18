package com.cormacx.timaoepumba.entities.aggregate;


import com.cormacx.timaoepumba.entities.account.DepositWithdrawal;
import com.cormacx.timaoepumba.entities.order.Order;
import lombok.Data;

@Data
public class PairOfOrderAndDepositWithdrawal {

    private DepositWithdrawal depositWithdrawal;

    private Order order;

    private Double financialResult;

    private long differenceInTime;

}
