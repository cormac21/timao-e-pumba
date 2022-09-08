package com.cormacx.timaoepumba.entities.operation;

import com.cormacx.timaoepumba.entities.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Min;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private Date createdOn;

    @ManyToMany(mappedBy = "operations")
    private List<Account> accounts;

}
