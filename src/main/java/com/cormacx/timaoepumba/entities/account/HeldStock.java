package com.cormacx.timaoepumba.entities.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "held_stocks")
@Data
@NoArgsConstructor
public class HeldStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    private String ticker;

    private Double totalPrice;

    private Double averagePrice;

    private Date lastAcquired;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Account account;

    public HeldStock(Integer quantity, String ticker, Double totalPrice, Double averagePrice, Date lastAcquired, Account account) {
        this.quantity = quantity;
        this.ticker = ticker;
        this.totalPrice = totalPrice;
        this.averagePrice = averagePrice;
        this.lastAcquired = lastAcquired;
        this.account = account;
    }
}
