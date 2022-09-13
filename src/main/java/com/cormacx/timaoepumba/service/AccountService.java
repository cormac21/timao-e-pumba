package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.AccountOperation;
import com.cormacx.timaoepumba.entities.account.OperationType;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
import com.cormacx.timaoepumba.repositories.AccountOperationRepository;
import com.cormacx.timaoepumba.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountOperationRepository operationRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountOperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
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
        account.setStocks(new ArrayList<>());
        account.setOperations(new ArrayList<>());
        account.setOrders(new ArrayList<>());
        return accountRepository.save(account);
    }

    public Account saveOrUpdateAccount(Account acc) {
        return accountRepository.save(acc);
    }

    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    public void addFundsToAccount(Account account, Double value) {
        AccountOperation operation = new AccountOperation();
        operation.setOperationType(OperationType.CREDIT);
        operation.setAmount(value);
        operation.setCreatedOn(new Date());
        operationRepository.save(operation);
        account.addBalance(value);
        account.addOperation(operation);
        accountRepository.save(account);
    }

    public void subtractFundsFromAccount(Account account, Double value) {
        AccountOperation operation = new AccountOperation();
        operation.setOperationType(OperationType.DEBIT);
        operation.setAmount(value);
        operation.setCreatedOn(new Date());
        operationRepository.save(operation);
        account.subtractBalance(value);
        account.addOperation(operation);
        accountRepository.save(account);
    }

    public void addOrderToAccount(Account account, Order saved) {
        account.addOrder(saved);
    }

    public void addAccountOperationToAccount(Order saved) {
        AccountOperation accountOperation = createAccountOperationBasedOnOrder(saved);

    }

    private AccountOperation createAccountOperationBasedOnOrder(Order saved) {
        AccountOperation accOp = new AccountOperation();
        accOp.setAmount(saved.getTotalPrice());
        accOp.setCreatedOn(saved.getCreatedOn());
        if( saved.getOpType() == OrderType.BUY ) {
            accOp.setOperationType(OperationType.DEBIT);
        } else {
            accOp.setOperationType(OperationType.CREDIT);
        }
        accOp.setAccount(saved.getAccount());
        return operationRepository.save(accOp);
    }
}
