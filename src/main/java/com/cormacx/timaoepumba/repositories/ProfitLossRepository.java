package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.aggregate.ProfitLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ProfitLossRepository extends JpaRepository<ProfitLoss, BigInteger> {

}
