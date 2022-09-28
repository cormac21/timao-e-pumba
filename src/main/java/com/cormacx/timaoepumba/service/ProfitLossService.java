package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.HeldStock;
import com.cormacx.timaoepumba.entities.aggregate.ProfitLoss;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.repositories.ProfitLossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfitLossService {

    private final ProfitLossRepository profitLossRepository;

    @Autowired
    public ProfitLossService(ProfitLossRepository profitLossRepository) {
        this.profitLossRepository = profitLossRepository;
    }


    public ProfitLoss registerProfitLoss(HeldStock accountHeldStock, Order order) {
        ProfitLoss profitLoss = new ProfitLoss(accountHeldStock.getAveragePrice(), accountHeldStock.getTotalPrice(),
                accountHeldStock.getLastAcquired(), accountHeldStock.getAccount().getId(), order);

        return save(profitLoss);
    }

    private ProfitLoss save(ProfitLoss profitLoss) {
        return profitLossRepository.save(profitLoss);
    }

    public List<ProfitLoss> getAllAccountsProfitLosses(Long accountId) {
        return profitLossRepository.findAllByAccountId(accountId);
    }
}
