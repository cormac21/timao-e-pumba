package com.cormacx.timaoepumba.entities.account;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigInteger;
import java.util.Date;

import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "deposit_withdrawal")
@Data
public class DepositWithdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    private OperationType operationType;

    private double amount;

    private Date createdOn;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Account account;

}
