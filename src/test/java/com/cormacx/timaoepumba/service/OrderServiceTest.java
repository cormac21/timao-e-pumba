package com.cormacx.timaoepumba.service;


import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderDTO;
import com.cormacx.timaoepumba.entities.order.OrderType;
import com.cormacx.timaoepumba.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AccountService accountService;

    OrderDTO incomingOrder;

    @BeforeEach
    public void setup() {
        Account account = new Account();
        account.setActive(true);
        account.setUserUUID("some-fake-user-uuid");
        account.setBalance(5000.33D);
        Optional<Account> accountOp = Optional.of(account);
        when(accountService.findAccountByUser("some-fake-user-uuid")).thenReturn(accountOp);

        Date createdOn = new Date();

        incomingOrder = new OrderDTO();
        incomingOrder.setQuantity(200);
        incomingOrder.setTicker("MGLU3");
        incomingOrder.setType("c");
        incomingOrder.setUserUUID("some-fake-user-uuid");
        incomingOrder.setUnitPrice(4.66D);
        Order order = new Order();
        order.setQuantity(200);
        order.setTicker("MGLU3");
        order.setType(OrderType.BUY);
        order.setUserUUID("some-fake-user-uuid");
        order.setUnitPrice(4.66D);
        order.setCreatedOn(createdOn);
        order.setAccount(account);
        order.setTotalPrice(932D);

        when(orderRepository.save(any(Order.class))).thenAnswer((Answer<Order>) invocation -> {
            Order order1 = invocation.getArgument(0);
            order1.setId(BigInteger.valueOf(1));
            return order1;
        });

    }

    @Test
    public void isThereEnoughBalanceForOrderTest(){
        boolean result = orderService.isThereEnoughBalanceOnAccount(1500.00D, 100, 20.5D);

        assertFalse(result);
    }

    @Test
    public void theresNotEnoughBalanceForNewOrderTest(){
        boolean result = orderService.isThereEnoughBalanceOnAccount(3500.99D, 100, 20.5D);

        assertTrue(result);
    }

    @Test
    public void shouldSaveNewHeldStockToAccountWhenProcessingBuyOrder() {
        Optional<Order> created = orderService.createNewOrder(incomingOrder);

        assertNotNull(created.get().getAccount());
    }

}
