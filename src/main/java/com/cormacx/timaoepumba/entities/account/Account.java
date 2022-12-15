package com.cormacx.timaoepumba.entities.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;



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
