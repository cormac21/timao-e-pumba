package com.cormacx.timaoepumba.entities.order;

import com.cormacx.timaoepumba.entities.account.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @NotNull
    private OrderType type;

    @NotNull
    @Min(value = 1L)
    private Integer quantity;

    @NotNull
    private String ticker;

    @NotNull
    private Double unitPrice;

    @NotNull
    private Double totalPrice;

    @NotNull
    private String userUUID;

    private Date createdOn;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    public Order(OrderType type, Integer quantity, String ticker, Double unitPrice, String userUUID, Date createdOn, Account account) {
        this.type = type;
        this.quantity = quantity;
        this.ticker = ticker;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * quantity;
        this.userUUID = userUUID;
        this.createdOn = createdOn;
        this.account = account;
    }
}
