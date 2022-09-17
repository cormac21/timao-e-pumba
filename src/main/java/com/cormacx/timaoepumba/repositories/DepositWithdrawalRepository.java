package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.account.DepositWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface DepositWithdrawalRepository extends JpaRepository<DepositWithdrawal, BigInteger> {
}
