package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderDTO;
import com.cormacx.timaoepumba.repositories.OrderRepository;
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

    private final OrderRepository orderRepository;

    private final AccountService accountService;

    @Autowired
    public OrderService(OrderRepository orderRepository, AccountService accountService) {
        this.orderRepository = orderRepository;
        this.accountService = accountService;
    }


    public Optional<Order> getOrderById(BigInteger id) {
        return orderRepository.findById(id);

    }

    public Optional<Order> createNewOrder(OrderDTO op) {
        op.setCreatedOn(new Date());
        if (isValidOrder(op)){
            Optional<Account> accountOp = accountService.findAccountByUser(op.getUserUUID());
            if(accountOp.isPresent()){
                Order toBeCreated = OrderDTO.toEntity(op, accountOp.get());
                Order saved = orderRepository.save(toBeCreated);
                processAccountOperationAndHeldStocks(accountOp.get(), saved);

                return Optional.of(saved);
            }
        }
        return Optional.empty();
    }

    private void processAccountOperationAndHeldStocks(Account account, Order savedOrder) {
        accountService.addOrderToAccount(account, savedOrder);
        accountService.addAccountOperationToAccount(savedOrder);
        accountService.subtractFundsFromAccount(account, savedOrder.getTotalPrice());

    }

    public boolean isValidOrder(OrderDTO op) {
        Optional<Account> accountOp = accountService.findAccountByUser(op.getUserUUID());
        if(accountOp.isEmpty()){
            return false;
        }
        if(!isThereEnoughBalanceOnAccount(accountOp.get().getBalance(), op.getQuantity(), op.getUnitPrice())) {
            return false;
        }
        return op.getOpType().equalsIgnoreCase("c") && op.getOpType().equalsIgnoreCase("v");
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
}
