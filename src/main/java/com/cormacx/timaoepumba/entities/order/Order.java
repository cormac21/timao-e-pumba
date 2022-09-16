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
    private OrderType opType;

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

}
