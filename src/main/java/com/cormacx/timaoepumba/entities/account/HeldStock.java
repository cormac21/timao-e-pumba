package com.cormacx.timaoepumba.entities.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.Date;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

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
