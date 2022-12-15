package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.DepositWithdrawal;
import com.cormacx.timaoepumba.entities.account.OperationType;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
import com.cormacx.timaoepumba.repositories.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private DepositWithdrawalService depositWithdrawalService;

    @Autowired
    public AccountService(AccountRepository accountRepository, DepositWithdrawalService depositWithdrawalService) {
        this.accountRepository = accountRepository;
        this.depositWithdrawalService = depositWithdrawalService;
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

    public Account saveOrUpdateAccount(Account acc) {
        return accountRepository.save(acc);
    }

    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public void addFundsToAccount(Account account, Double value) {
        account.addBalance(value);
        accountRepository.save(account);
    }

    @Transactional
    public void subtractFundsFromAccount(Account account, Double value) {
        account.subtractBalance(value);
        accountRepository.save(account);
    }

    public void addNewDepositWithdrawalAndUpdateBalance(Order saved) {
        createDepositWithdrawalBasedOnOrder(saved);
        if(saved.getAccount() == null) {
            throw new RuntimeException();
        } else {
            Optional<Account> deFactoAccount = accountRepository.findById(saved.getAccount().getId());
            if(deFactoAccount.isPresent()){
                if(saved.getType() == OrderType.BUY) {
                    subtractFundsFromAccount(deFactoAccount.get(), saved.getTotalPrice());
                } else if (saved.getType() == OrderType.SELL){
                    addFundsToAccount(deFactoAccount.get(), saved.getTotalPrice());
                }
            }
        }
    }

    private void createDepositWithdrawalBasedOnOrder(Order saved) {
        DepositWithdrawal depositWithdrawal = new DepositWithdrawal();
        depositWithdrawal.setAmount(saved.getTotalPrice());
        depositWithdrawal.setCreatedOn(saved.getCreatedOn());
        if( saved.getType() == OrderType.BUY ) {
            depositWithdrawal.setOperationType(OperationType.DEBIT);
        } else {
            depositWithdrawal.setOperationType(OperationType.CREDIT);
        }
        depositWithdrawal.setAccount(saved.getAccount());
        depositWithdrawalService.createNewDepositWithdrawal(depositWithdrawal);
    }

}
