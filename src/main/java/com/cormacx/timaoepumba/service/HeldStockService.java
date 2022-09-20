package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.HeldStock;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.repositories.HeldStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeldStockService {

    private final HeldStockRepository heldStockRepository;

    @Autowired
    public HeldStockService(HeldStockRepository heldStockRepository) {
        this.heldStockRepository = heldStockRepository;
    }

    public HeldStock createOrUpdateHeldStock(Order savedOrder) {


        return null;
    }



}
