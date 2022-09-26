package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.HeldStock;
import com.cormacx.timaoepumba.entities.account.HeldStockStatus;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
import com.cormacx.timaoepumba.repositories.HeldStockRepository;
import exceptions.InvalidOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HeldStockServiceTest {

    @InjectMocks
    HeldStockService heldStockService;

    @Mock
    HeldStockRepository heldStockRepository;

    @Mock
    ProfitLossService profitLossService;

    private String randomUserUUID = UUID.randomUUID().toString();
    private HeldStock exampleStock;

    private Order exampleBuyOrder;

    private Date exampleBuyOrderDate = new Date();

    private Account account;

    private boolean setup;
    @BeforeEach
    public void setup() {
        if (!setup) {
            account = new Account();
            account.setId(5L);
            account.setUserUUID(randomUserUUID);
            account.setBalance(23489D);
            account.setActive(true);

            exampleStock = new HeldStock();
            exampleStock.setQuantity(100);
            exampleStock.setTicker("MGLU3");
            exampleStock.setAveragePrice(4.7D);
            exampleStock.setTotalPrice(470D);
            exampleStock.setAccount(account);
            exampleStock.setLastAcquired(new Date());

            exampleBuyOrder = new Order();
            exampleBuyOrder.setType(OrderType.BUY);
            exampleBuyOrder.setAccount(account);
            exampleBuyOrder.setQuantity(100);
            exampleBuyOrder.setUnitPrice(4.7D);
            exampleBuyOrder.setTotalPrice(470D);
            exampleBuyOrder.setUserUUID(randomUserUUID);
            exampleBuyOrder.setTicker("MGLU3");
            exampleBuyOrder.setCreatedOn(exampleBuyOrderDate);

            setup = true;
        }
    }

    @Test
    public void canSaveNewHeldStockOnBuyOrder(){
        when(heldStockRepository.save(any(HeldStock.class))).thenAnswer((Answer<HeldStock>) invocation -> {
            HeldStock stock = invocation.getArgument(0);
            stock.setId(1L);
            return stock;
        });
        Date date = new Date();
        Order order = new Order(OrderType.BUY, 400, "EGIE3", 40.70D,
                randomUserUUID, date, account);

        HeldStock saved = heldStockService.createOrUpdateHeldStock(order);

        HeldStock expected = new HeldStock(400, "EGIE3", 16280D, 40.7D, date, account, HeldStockStatus.HELD);
        expected.setId(1L);
        verify(heldStockRepository, times(1)).save(expected);
        reset(heldStockRepository);
    }

    @Test
    public void canCreateNewHeldStockBasedOnOrder() {
        when(heldStockRepository.save(any(HeldStock.class))).thenAnswer((Answer<HeldStock>) invocation -> {
            HeldStock stock = invocation.getArgument(0);
            stock.setId(1L);
            return stock;
        });
        when(heldStockRepository.findAllByAccount(any(Account.class))).thenReturn(Collections.emptyList());
        Date orderDate = new Date();
        Order incomingBuyOrder = new Order(OrderType.BUY, 300, "MGLU3", 5.1D,
                randomUserUUID, orderDate, account);

        HeldStock saved = heldStockService.createOrUpdateHeldStock(incomingBuyOrder);

        assertEquals(300, saved.getQuantity());
        assertEquals("MGLU3", saved.getTicker());
        assertEquals(1530D, saved.getTotalPrice());
        assertEquals(5.1D, saved.getAveragePrice());
        assertEquals(account , saved.getAccount());
        assertEquals(HeldStockStatus.HELD, saved.getStatus());
        assertEquals(orderDate, saved.getLastAcquired());
    }

    @Test
    public void canMergeHeldStockWithIncomingBuyOrder() {
        when(heldStockRepository.save(any(HeldStock.class))).thenAnswer((Answer<HeldStock>) invocation -> {
            HeldStock stock = invocation.getArgument(0);
            stock.setId(1L);
            return stock;
        });
        when(heldStockRepository.findAllByAccount(any(Account.class))).thenReturn(Arrays.asList(exampleStock));
        Order order = new Order(OrderType.BUY, 200, "MGLU3", 4.9D, randomUserUUID,
                new Date(), account);

        HeldStock resultingStock = heldStockService.createOrUpdateHeldStock(order);

        assertEquals(300, resultingStock.getQuantity());
        assertEquals("MGLU3", resultingStock.getTicker());
        assertEquals(1450D, resultingStock.getTotalPrice());
        assertEquals(resultingStock.getLastAcquired(), order.getCreatedOn());
        assertEquals(4.83D, resultingStock.getAveragePrice());
        reset(heldStockRepository);
    }

    @Test
    public void canMergeStockWithIncomingSellOrder() {
        when(heldStockRepository.save(any(HeldStock.class))).thenAnswer((Answer<HeldStock>) invocation -> {
            HeldStock stock = invocation.getArgument(0);
            stock.setId(1L);
            return stock;
        });
        when(heldStockRepository.findAllByAccount(any(Account.class))).thenReturn(Arrays.asList(exampleStock));
        Order order = new Order(OrderType.SELL, 50, "MGLU3", 5.26D, randomUserUUID,
                new Date(), account);

        HeldStock resultingStock = heldStockService.createOrUpdateHeldStock(order);
        assertEquals(50, resultingStock.getQuantity());
        assertEquals("MGLU3", resultingStock.getTicker());
        assertEquals(235D, resultingStock.getTotalPrice());
        assertEquals( 4.7D, resultingStock.getAveragePrice());
        reset(heldStockRepository);
    }

    @Test
    public void closesHeldStockIfIncomingSellOrderSellsEverything() {
        when(heldStockRepository.save(any(HeldStock.class))).thenAnswer((Answer<HeldStock>) invocation -> {
            HeldStock stock = invocation.getArgument(0);
            stock.setId(1L);
            return stock;
        });
        when(heldStockRepository.findAllByAccount(any(Account.class))).thenReturn(Arrays.asList(exampleStock));
        Order order = new Order(OrderType.SELL, 100, "MGLU3", 4.9D, randomUserUUID,
                new Date(), account);

        HeldStock resultingStock = heldStockService.createOrUpdateHeldStock(order);

        assertEquals(HeldStockStatus.CLOSED, resultingStock.getStatus());
        reset(heldStockRepository);
    }

    @Test
    public void callsProfitLossServiceIfSellOrder(){
        when(heldStockRepository.save(any(HeldStock.class))).thenAnswer((Answer<HeldStock>) invocation -> {
            HeldStock stock = invocation.getArgument(0);
            stock.setId(1L);
            return stock;
        });
        when(heldStockRepository.findAllByAccount(any(Account.class))).thenReturn(Arrays.asList(exampleStock));

        Order order = new Order(OrderType.SELL, 100, "MGLU3", 5.26D, randomUserUUID,
                new Date(), account);
        HeldStock resultingStock = new HeldStock(0, "MGLU3", 0D, 4.7D,
                exampleStock.getLastAcquired(), exampleStock.getAccount(), HeldStockStatus.CLOSED);
        resultingStock.setId(1L);

        heldStockService.saveHeldStockAndCalculateProfitLoss(order);

        verify(profitLossService, times(1)).registerProfitLoss(resultingStock, order);
        reset(heldStockRepository);
    }

    @Test
    public void throwsExceptionIfOrderHasNoAccount() {
        Order order = new Order(OrderType.BUY, 200, "MGLU3", 4.9D, randomUserUUID,
                new Date(), null);

        assertThrows(InvalidOrderException.class, () -> {
            heldStockService.createOrUpdateHeldStock(order);
        });
    }

    @Test
    public void throwsExceptionIfSellOrderHasMoreQuantityThanExisting() {
        Order order = new Order(OrderType.SELL, 200, "MGLU3", 4.9D, randomUserUUID,
                new Date(), account);
        when(heldStockRepository.findAllByAccount(any(Account.class))).thenReturn(Arrays.asList(exampleStock));

        assertThrows(InvalidOrderException.class, () -> {
            heldStockService.createOrUpdateHeldStock(order);
        });
        reset(heldStockRepository);
    }

}
