package com.cormacx.timaoepumba.entities.account;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
public class AccountOperation {

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
