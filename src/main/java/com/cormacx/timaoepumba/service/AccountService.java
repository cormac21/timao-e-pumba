package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.AccountOperation;
import com.cormacx.timaoepumba.entities.account.OperationType;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
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

    public Account saveOrUpdateAccount(Account acc) {
        return accountRepository.save(acc);
    }

    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    public void addFundsToAccount(Account account, Double value) {
        AccountOperation operation = new AccountOperation();
        operation.setOperationType(OperationType.CREDIT);
        operation.setAmount(value);
        operation.setCreatedOn(new Date());
        operation.setAccount(account);
        account.addOperation(operation);
        account.addBalance(value);
        accountRepository.save(account);
    }

    public void subtractFundsFromAccount(Account account, Double value) {
        AccountOperation operation = new AccountOperation();
        operation.setOperationType(OperationType.DEBIT);
        operation.setAmount(value);
        operation.setCreatedOn(new Date());
        operation.setAccount(account);
        account.subtractBalance(value);
        account.addOperation(operation);
        accountRepository.save(account);
    }

    public void addOrderToAccount(Order saved) {
        saved.getAccount().addOrder(saved);
    }

    public void addAccountOperationToAccount(Order saved) {
        createAccountOperationBasedOnOrder(saved);

    }

    private void createAccountOperationBasedOnOrder(Order saved) {
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setAmount(saved.getTotalPrice());
        accountOperation.setCreatedOn(saved.getCreatedOn());
        if( saved.getOpType() == OrderType.BUY ) {
            accountOperation.setOperationType(OperationType.DEBIT);
        } else {
            accountOperation.setOperationType(OperationType.CREDIT);
        }
        accountOperation.setAccount(saved.getAccount());
        saved.getAccount().addOperation(accountOperation);
        accountRepository.save(saved.getAccount());
    }
}
