package com.cormacx.timaoepumba.entities.account;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "held_stocks")
@Data
public class HeldStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    private String ticker;

    private String stockName;

    private Double totalPrice;

    private Double averagePrice;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Account account;

}
