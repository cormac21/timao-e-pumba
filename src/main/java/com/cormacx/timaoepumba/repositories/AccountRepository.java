package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUser(String userUUID);

}
