package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> findAccountByUser(String userUUID) {
        Optional<Account> accountOp = accountRepository.findByUserUUID(userUUID);
        return accountOp;
    }

    public Account createNewAccount(String userUUID) {
        Account account = new Account();
        account.setUserUUID(userUUID);
        account.setBalance(0D);
        account.setActive(true);
        return accountRepository.save(account);
    }

    public Account saveOrUpdateAccount(Account acc){
        return accountRepository.save(acc);
    }

    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }
}
