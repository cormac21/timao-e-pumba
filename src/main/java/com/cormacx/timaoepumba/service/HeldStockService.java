package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.entities.account.HeldStock;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
import com.cormacx.timaoepumba.repositories.HeldStockRepository;
import exceptions.InvalidOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class HeldStockService {

    private final HeldStockRepository heldStockRepository;

    private final ProfitLossService profitLossService;

    private final AccountService accountService;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Autowired
    public HeldStockService(HeldStockRepository heldStockRepository, ProfitLossService profitLossService, AccountService accountService) {
        this.heldStockRepository = heldStockRepository;
        this.profitLossService = profitLossService;
        this.accountService = accountService;
    }

    public HeldStock createOrUpdateHeldStock(Order savedOrder) {
        if(savedOrder.getAccount() == null){
           throw new InvalidOrderException();
        }
        List<HeldStock> accountHeldStock = heldStockRepository.findAllByAccount(savedOrder.getAccount());
        if (accountHasSameStockInInventory(accountHeldStock, savedOrder.getTicker())) {
            HeldStock previouslyHeldStock = selectSameStock(accountHeldStock, savedOrder.getTicker());
            if(savedOrder.getType() == OrderType.SELL) {
                if(savedOrder.getQuantity() > previouslyHeldStock.getQuantity()) {
                    throw new InvalidOrderException();
                }
                profitLossService.registerProfitLoss(previouslyHeldStock, savedOrder);
            }
            HeldStock resultingMerge = mergeHeldStock(savedOrder, previouslyHeldStock);
            if(resultingMerge.getQuantity() == 0) {
                remove(resultingMerge);
            } else {
                return save(resultingMerge);
            }
        } else {
            HeldStock noMergeStock = new HeldStock(
                    savedOrder.getQuantity(), savedOrder.getTicker(),
                    savedOrder.getTotalPrice(), savedOrder.getUnitPrice(),
                    savedOrder.getCreatedOn(), savedOrder.getAccount()
            );
            return save(noMergeStock);
        }
        return null;
    }

    private HeldStock mergeHeldStock(Order savedOrder, HeldStock heldStock) {
        Integer combinedQuantity = 0;
        double combinedTotalPrice = 0.0D;
        double combinedAveragePrice = 0.0D;
        if (savedOrder.getType() == OrderType.BUY){
            combinedQuantity = savedOrder.getQuantity() + heldStock.getQuantity();
            combinedTotalPrice = new BigDecimal(savedOrder.getTotalPrice() + heldStock.getTotalPrice())
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
            combinedAveragePrice = new BigDecimal(combinedTotalPrice / combinedQuantity)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
            heldStock.setLastAcquired(savedOrder.getCreatedOn());
        } else if (savedOrder.getType() == OrderType.SELL) {
            combinedQuantity = heldStock.getQuantity() - savedOrder.getQuantity();
            combinedAveragePrice = heldStock.getAveragePrice();
            combinedTotalPrice = combinedAveragePrice * combinedQuantity;
        } else {
            return null;
        }
        heldStock.setQuantity(combinedQuantity);
        heldStock.setTotalPrice(combinedTotalPrice);
        heldStock.setAveragePrice(combinedAveragePrice);
        return heldStock;
    }

    private HeldStock selectSameStock(List<HeldStock> accountHeldStock, String ticker) {
        for (HeldStock stock : accountHeldStock) {
            if (stock.getTicker().equals(ticker)){
                return stock;
            }
        }
        return null;
    }

    private boolean accountHasSameStockInInventory(List<HeldStock> accountHeldStock, String ticker) {
        for (HeldStock stock : accountHeldStock) {
            if (stock.getTicker().equals(ticker)) {
                return true;
            }
        }
        return false;
    }

    private HeldStock save(HeldStock heldStock) {
        return heldStockRepository.save(heldStock);
    }

    private void remove(HeldStock heldStock) {
        heldStockRepository.delete(heldStock);
    }

    public List<HeldStock> getAllHeldStocksOnAccount(Long accountId) {
        Optional<Account> accountOptional = accountService.findAccountById(accountId);
        if ( accountOptional.isPresent() ) {
            return heldStockRepository.findAllByAccount(accountOptional.get());
        } else {
            return Collections.emptyList();
        }
    }

}
