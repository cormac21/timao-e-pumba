package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderDTO;
import com.cormacx.timaoepumba.entities.order.OrderType;
import com.cormacx.timaoepumba.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final AccountService accountService;

    private final HeldStockService heldStockService;

    @Autowired
    public OrderService(OrderRepository orderRepository, AccountService accountService, HeldStockService heldStockService) {
        this.orderRepository = orderRepository;
        this.accountService = accountService;
        this.heldStockService = heldStockService;
    }


    public Optional<Order> getOrderById(BigInteger id) {
        return orderRepository.findById(id);

    }

    public Optional<Order> createNewOrder(OrderDTO op) {
        op.setCreatedOn(new Date());
        if (isValidOrder(op)){
            Optional<Account> accountOp = accountService.findAccountByUser(op.getUserUUID());
            if(accountOp.isPresent()){
                Order saved = orderRepository.save(OrderDTO.toEntity(op, accountOp.get()));
                processAccountOperationAndHeldStocks(saved);
                return Optional.of(saved);
            }
        }
        return Optional.empty();
    }

    private void processAccountOperationAndHeldStocks(Order savedOrder) {
        accountService.addNewDepositWithdrawalAndUpdateBalance(savedOrder);
        heldStockService.createOrUpdateHeldStock(savedOrder);
    }

    public boolean isValidOrder(OrderDTO op) {
        Optional<Account> accountOp = accountService.findAccountByUser(op.getUserUUID());
        if(accountOp.isEmpty()){
            return false;
        }
        if (op.getType().equalsIgnoreCase("c")) {
            if(!isThereEnoughBalanceOnAccount(accountOp.get().getBalance(), op.getQuantity(), op.getUnitPrice())) {
                return false;
            }
        } else if (!op.getType().equalsIgnoreCase("v")) {
            return false;
        }
        return true;
    }

    public boolean isThereEnoughBalanceOnAccount(Double balance, Integer quantity, Double unitPrice) {
        Double totalPrice = quantity.doubleValue() * unitPrice;
        return balance.compareTo(totalPrice) >= 0;
    }

    public List<Order> getAllOrders(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Order> pageOfOrders = orderRepository.findAll(paging);

        if( pageOfOrders.hasContent()){
            return pageOfOrders.stream().toList();
        } else {
            return new ArrayList<>();
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getLatestBuyOrderFromUserForTicker(String userUUID, String ticker) {
        return orderRepository.findFirstByUserUUIDAndTickerAndTypeOrderByCreatedOnDesc(userUUID, ticker, OrderType.BUY);
    }

    public Order getLatestSellOrderFromUserForTicker(String userUUID, String ticker) {
        return orderRepository.findFirstByUserUUIDAndTickerAndTypeOrderByCreatedOnDesc(userUUID, ticker, OrderType.SELL);
    }

    public List<Order> getAllOrdersFromUser(Integer pageNumber, Integer pageSize, String sortBy, String userUUID) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return orderRepository.findAllByUserUUID(userUUID, paging);
    }
}
