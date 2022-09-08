package com.cormacx.timaoepumba.entities.account;

import com.cormacx.timaoepumba.entities.operation.Operation;
import com.cormacx.timaoepumba.entities.user.UserEntity;
import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private UserEntity user;

    private Double balance;

    @ManyToMany
    @JoinTable(
            name = "accounts_operations",
            joinColumns = @JoinColumn(
                    name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "operation_id", referencedColumnName = "id"))
    private List<Operation> operations;

}
