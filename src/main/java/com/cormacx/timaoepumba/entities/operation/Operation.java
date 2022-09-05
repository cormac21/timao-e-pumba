package com.cormacx.timaoepumba.entities.operation;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import java.math.BigInteger;

@Entity
@Data
public class Operation {

    @Id
    @GeneratedValue
    private BigInteger id;

    @NonNull
    private OperationType opType;

    @NonNull
    @Min(value = 1L)
    private Integer quantity;

    @NonNull
    private String ticker;

    @NonNull
    private Double unitPrice;

    @NonNull
    private Double totalPrice;

    @NonNull
    private String userUUID;

    public Operation() {}
}
