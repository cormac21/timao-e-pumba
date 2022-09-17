package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.DepositWithdrawal;
import com.cormacx.timaoepumba.entities.account.OperationType;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
import com.cormacx.timaoepumba.repositories.DepositWithdrawalRepository;
import com.cormacx.timaoepumba.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private DepositWithdrawalRepository depositWithdrawalRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, DepositWithdrawalRepository depositWithdrawalRepository) {
        this.accountRepository = accountRepository;
        this.depositWithdrawalRepository = depositWithdrawalRepository;
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
        createCreditForAccount(account, value);
        account.addBalance(value);
        accountRepository.save(account);
    }

    private void createCreditForAccount(Account account, Double value) {
        DepositWithdrawal deposit = new DepositWithdrawal();
        deposit.setOperationType(OperationType.CREDIT);
        deposit.setAmount(value);
        deposit.setAccount(account);
        deposit.setCreatedOn(new Date());
        depositWithdrawalRepository.save(deposit);
    }

    @Transactional
    public void subtractFundsFromAccount(Account account, Double value) {
        createDebitForAccount(account, value);
        account.subtractBalance(value);
        accountRepository.save(account);
    }

    private void createDebitForAccount(Account account, Double value) {
        DepositWithdrawal deposit = new DepositWithdrawal();
        deposit.setOperationType(OperationType.DEBIT);
        deposit.setAmount(value);
        deposit.setAccount(account);
        deposit.setCreatedOn(new Date());
        depositWithdrawalRepository.save(deposit);
    }



    public void addAccountOperationToAccount(Order saved) {
        createAccountOperationBasedOnOrder(saved);

    }

    private void createAccountOperationBasedOnOrder(Order saved) {
        DepositWithdrawal depositWithdrawal = new DepositWithdrawal();
        depositWithdrawal.setAmount(saved.getTotalPrice());
        depositWithdrawal.setCreatedOn(saved.getCreatedOn());
        if( saved.getOpType() == OrderType.BUY ) {
            depositWithdrawal.setOperationType(OperationType.DEBIT);
        } else {
            depositWithdrawal.setOperationType(OperationType.CREDIT);
        }
        depositWithdrawal.setAccount(saved.getAccount());
        accountRepository.save(saved.getAccount());
    }
}
