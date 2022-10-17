package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.aggregate.ProfitLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public interface ProfitLossRepository extends JpaRepository<ProfitLoss, BigInteger> {

    List<ProfitLoss> findAllByAccountId(Long accountId);

    List<ProfitLoss> findProfitLossesByAccountIdAndSellOrder_CreatedOnBetween(long accountId, Date sellOrder_createdOn,
                                                                              Date sellOrder_createdOn2);

}
