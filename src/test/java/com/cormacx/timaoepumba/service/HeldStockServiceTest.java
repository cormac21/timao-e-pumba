package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.HeldStock;
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

    private String randomUserUUID = UUID.randomUUID().toString();
    private HeldStock exampleStock;

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
            exampleStock.setQuantity(100L);
            exampleStock.setTicker("MGLU3");
            exampleStock.setAveragePrice(4.7D);
            exampleStock.setTotalPrice(470D);
            exampleStock.setAccount(account);
            exampleStock.setLastAcquired(new Date());

            setup = true;
        }
    }

    @Test
    public void canSaveNewHeldStock(){
        when(heldStockRepository.save(any(HeldStock.class))).thenAnswer((Answer<HeldStock>) invocation -> {
            HeldStock stock = invocation.getArgument(0);
            stock.setId(1L);
            return stock;
        });

        HeldStock saved = heldStockService.save(exampleStock);

        verify(heldStockRepository, times(1)).save(exampleStock);
        assertNotNull(saved.getId() != null);
        reset(heldStockRepository);
    }

    @Test
    public void canMergeHeldStockWithIncomingBuyOrder() {
        when(heldStockRepository.save(any(HeldStock.class))).thenAnswer((Answer<HeldStock>) invocation -> {
            HeldStock stock = invocation.getArgument(0);
            stock.setId(1L);
            return stock;
        });
        heldStockService.save(exampleStock);
        Order order = new Order(OrderType.BUY, 200, "MGLU3", 4.9D, 980D, randomUserUUID,
                new Date(), account);
        when(heldStockRepository.findAllByAccount(any(Account.class))).thenReturn(Arrays.asList(exampleStock));

        HeldStock resultingStock = heldStockService.createOrUpdateHeldStock(order);

        assertEquals(300, resultingStock.getQuantity());
        assertEquals("MGLU3", resultingStock.getTicker());
        assertEquals(1450D, resultingStock.getTotalPrice());
        assertEquals(resultingStock.getLastAcquired(), order.getCreatedOn());
        assertEquals(4.83D, resultingStock.getAveragePrice());
        reset(heldStockRepository);
    }

    @Test
    public void throwsExceptionIfOrderHasNoAccount() {
        Order order = new Order(OrderType.BUY, 200, "MGLU3", 4.9D, 980D, randomUserUUID,
                new Date(), null);

        assertThrows(InvalidOrderException.class, () -> {
            heldStockService.createOrUpdateHeldStock(order);
        });
    }

    @Test
    public void throwsExceptionIfSellOrderHasMoreQuantityThanExisting() {
        Order order = new Order(OrderType.SELL, 200, "MGLU3", 4.9D, 980D, randomUserUUID,
                new Date(), account);
        when(heldStockRepository.findAllByAccount(any(Account.class))).thenReturn(Arrays.asList(exampleStock));

        assertThrows(InvalidOrderException.class, () -> {
            heldStockService.createOrUpdateHeldStock(order);
        });
        reset(heldStockRepository);
    }

}
