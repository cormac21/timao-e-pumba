package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.AccountOperation;
import com.cormacx.timaoepumba.entities.account.OperationType;
import com.cormacx.timaoepumba.repositories.AccountOperationRepository;
import com.cormacx.timaoepumba.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    AccountOperationRepository accountOperationRepository;

    private String randomUserUUID;

    @BeforeEach
    public void setup() {
        randomUserUUID = UUID.randomUUID().toString();
        Account account = new Account();
        account.setUserUUID(randomUserUUID);
        account.setBalance(0D);
        account.setActive(true);
        account.setStocks(new ArrayList<>());
        account.setOperations(new ArrayList<>());
        account.setOrders(new ArrayList<>());
        when(accountRepository.save(any(Account.class))).thenReturn(account);
    }


    @Test
    public void canSaveNewAccount(){
        Account created = accountService.createNewAccount(randomUserUUID);

        assertTrue(created.isActive());
        assertEquals(0D, created.getBalance());
        assertEquals(new ArrayList<>(), created.getOperations());
        assertEquals(new ArrayList<>(), created.getStocks());
        assertEquals(new ArrayList<>(), created.getOrders());
    }

    @Test
    public void canDepositFundsOnNewAccount() {
        AccountOperation operation = new AccountOperation();
        operation.setOperationType(OperationType.CREDIT);
        operation.setAmount(2400.94);
        operation.setCreatedOn(new Date());
        when(accountOperationRepository.save(any(AccountOperation.class))).thenReturn(operation);

        Account created = accountService.createNewAccount(randomUserUUID);

        accountService.addFundsToAccount(created, 2400.94);

        assertEquals(1, created.getOperations().size());
        assertEquals(2400.94, created.getBalance());
    }

}
