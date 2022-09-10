package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.account.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation, BigInteger> {
}
