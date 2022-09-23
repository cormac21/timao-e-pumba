package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.HeldStock;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
import com.cormacx.timaoepumba.repositories.HeldStockRepository;
import exceptions.InvalidOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeldStockService {

    private final HeldStockRepository heldStockRepository;

    @Autowired
    public HeldStockService(HeldStockRepository heldStockRepository) {
        this.heldStockRepository = heldStockRepository;
    }

    public HeldStock createOrUpdateHeldStock(Order savedOrder) {
        if(savedOrder.getAccount() == null){
           throw new InvalidOrderException();
        }
        List<HeldStock> accountHeldStock = heldStockRepository.findAllByAccount(savedOrder.getAccount());
        if (accountHasSameStockInInventory(accountHeldStock, savedOrder.getTicker())) {
            HeldStock previouslyHeldStock = selectSameStock(accountHeldStock, savedOrder.getTicker());
            if(savedOrder.getType() == OrderType.SELL && savedOrder.getQuantity() > previouslyHeldStock.getQuantity()) {
                throw new InvalidOrderException();
            }
            HeldStock resultingMerge = mergeHeldStock(savedOrder, previouslyHeldStock);
            return save(resultingMerge);
        }
        return null;
    }

    private HeldStock mergeHeldStock(Order savedOrder, HeldStock heldStock) {
        Integer combinedQuantity = savedOrder.getQuantity() + heldStock.getQuantity();
        double combinedTotalPrice = savedOrder.getTotalPrice() + heldStock.getTotalPrice();
        double combinedAveragePrice = combinedTotalPrice / combinedQuantity;

        heldStock.setQuantity(combinedQuantity);
        heldStock.setTotalPrice(combinedTotalPrice);
        heldStock.setAveragePrice(combinedAveragePrice);
        heldStock.setLastAcquired(savedOrder.getCreatedOn());
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

    public HeldStock save(HeldStock heldStock) {
        return heldStockRepository.save(heldStock);
    }
}
