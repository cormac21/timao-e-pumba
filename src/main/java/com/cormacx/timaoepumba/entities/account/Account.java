package com.cormacx.timaoepumba.entities.account;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public void addBalance(Double value) {
        balance = balance + value;
    }

    public void subtractBalance(Double value) {
        balance = balance - value;
    }

    public Account(@NonNull String userUUID, Double balance, boolean active) {
        this.userUUID = userUUID;
        this.balance = balance;
        this.active = active;
    }
}
