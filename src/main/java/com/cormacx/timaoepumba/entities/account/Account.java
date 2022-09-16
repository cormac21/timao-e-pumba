package com.cormacx.timaoepumba.entities.account;

import com.cormacx.timaoepumba.entities.order.Order;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String userUUID;

    private Double balance;

    private boolean active;

    @OneToMany(mappedBy = "account")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "account")
    private Set<AccountOperation> operations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "account")
    private Set<HeldStock> stocks = new LinkedHashSet<>();

    public void addOperation(AccountOperation accountOperation) {
        operations.add(accountOperation);
    }

    public void addBalance(Double value) {
        balance = balance + value;
    }

    public void subtractBalance(Double value) {
        balance = balance - value;
    }

    public void addOrder(Order saved) {
        orders.add(saved);
    }
}
