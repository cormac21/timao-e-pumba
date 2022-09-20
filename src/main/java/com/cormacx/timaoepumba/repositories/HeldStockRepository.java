package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.HeldStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeldStockRepository extends JpaRepository<HeldStock, Long> {

    List<HeldStock> findAllByAccount(Account account);

    List<HeldStock> findAllByAccount_UserUUID(String userUUID);

}
