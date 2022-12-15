package com.cormacx.timaoepumba.entities.order;

import com.cormacx.timaoepumba.entities.account.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
        this.totalPrice = Math.floor(unitPrice * quantity);
        this.userUUID = userUUID;
        this.createdOn = createdOn;
        this.account = account;
    }
}
