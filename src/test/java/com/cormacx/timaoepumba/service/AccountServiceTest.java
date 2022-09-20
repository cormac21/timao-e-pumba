package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.DepositWithdrawal;
import com.cormacx.timaoepumba.entities.account.OperationType;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
import com.cormacx.timaoepumba.repositories.AccountRepository;
import com.cormacx.timaoepumba.repositories.DepositWithdrawalRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    DepositWithdrawalRepository depositWithdrawalRepository;

    private String randomUserUUID;

    Account account;
    Order buyOrder;
    Order sellOrder;

    Date now;

    @BeforeAll
    public void setup() {
        randomUserUUID = UUID.randomUUID().toString();
        account = new Account();
        account.setUserUUID(randomUserUUID);
        account.setBalance(0D);
        account.setActive(true);
        account.setId(1L);

        now = new Date();

        buyOrder = new Order();
        buyOrder.setAccount(account);
        buyOrder.setTicker("EGIE3");
        buyOrder.setUnitPrice(45.2D);
        buyOrder.setType(OrderType.BUY);
        buyOrder.setUserUUID(randomUserUUID);
        buyOrder.setQuantity(300);
        buyOrder.setTotalPrice(13560D);
        buyOrder.setCreatedOn(now);

        sellOrder = new Order();
        sellOrder.setAccount(account);
        sellOrder.setTicker("EGIE3");
        sellOrder.setUnitPrice(45.2D);
        sellOrder.setType(OrderType.SELL);
        sellOrder.setUserUUID(randomUserUUID);
        sellOrder.setQuantity(200);
        sellOrder.setTotalPrice(9040D);
        sellOrder.setCreatedOn(now);
    }

    @Test
    public void canSaveNewAccountAndBalanceIsZeroed(){
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        Account created = accountService.createNewAccount(randomUserUUID);

        assertTrue(created.isActive());
        assertEquals(0D, created.getBalance());
        assertEquals(randomUserUUID, created.getUserUUID());
    }

    @Test
    public void canDepositFundsOnNewAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        Account created = accountService.createNewAccount(randomUserUUID);

        accountService.addFundsToAccount(created, 2400.94);

        assertEquals(2400.94, created.getBalance());
    }

    @Test
    public void whenProcessingBuyOrderCreatesADebitWithdraw() {
        accountService.addAccountOperationToAccount(buyOrder);
        DepositWithdrawal expected = new DepositWithdrawal();
        expected.setAccount(account);
        expected.setCreatedOn(now);
        expected.setAmount(13560D);
        expected.setOperationType(OperationType.DEBIT);
        expected.setAccount(account);
        verify(depositWithdrawalRepository, times(1)).save(expected);
    }

    @Test
    public void whenProcessingSellOrderCreatesACreditDeposit(){
        accountService.addAccountOperationToAccount(sellOrder);
        DepositWithdrawal expected = new DepositWithdrawal();
        expected.setAccount(account);
        expected.setCreatedOn(now);
        expected.setAmount(9040D);
        expected.setOperationType(OperationType.CREDIT);
        expected.setAccount(account);
        verify(depositWithdrawalRepository, times(1)).save(expected);
    }

}
