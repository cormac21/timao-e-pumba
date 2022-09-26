package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.HeldStock;
import com.cormacx.timaoepumba.entities.account.HeldStockStatus;
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

    private final ProfitLossService profitLossService;

    @Autowired
    public HeldStockService(HeldStockRepository heldStockRepository, ProfitLossService profitLossService) {
        this.heldStockRepository = heldStockRepository;
        this.profitLossService = profitLossService;
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
            }
            HeldStock resultingMerge = mergeHeldStock(savedOrder, previouslyHeldStock);
            return save(resultingMerge);
        } else {
            HeldStock noMergeStock = new HeldStock(
                    savedOrder.getQuantity(), savedOrder.getTicker(),
                    savedOrder.getTotalPrice(), savedOrder.getUnitPrice(),
                    savedOrder.getCreatedOn(), savedOrder.getAccount(),
                    HeldStockStatus.HELD
            );
            return save(noMergeStock);
        }
    }

    public void saveHeldStockAndCalculateProfitLoss(Order savedOrder) {
        HeldStock heldStock = createOrUpdateHeldStock(savedOrder);
        if(heldStock != null) {
            profitLossService.registerProfitLoss(heldStock, savedOrder);
        }
    }

    private HeldStock mergeHeldStock(Order savedOrder, HeldStock heldStock) {
        Integer combinedQuantity = 0;
        double combinedTotalPrice = 0.0D;
        double combinedAveragePrice = 0.0D;
        if (savedOrder.getType() == OrderType.BUY){
            combinedQuantity = savedOrder.getQuantity() + heldStock.getQuantity();
            combinedTotalPrice = savedOrder.getTotalPrice() + heldStock.getTotalPrice();
            combinedAveragePrice = combinedTotalPrice / combinedQuantity;
            heldStock.setLastAcquired(savedOrder.getCreatedOn());
            heldStock.setStatus(HeldStockStatus.HELD);
        } else if (savedOrder.getType() == OrderType.SELL) {
            combinedQuantity = heldStock.getQuantity() - savedOrder.getQuantity();
            combinedAveragePrice = heldStock.getAveragePrice();
            if (combinedQuantity == 0) {
                heldStock.setStatus(HeldStockStatus.CLOSED);
            }
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



}
