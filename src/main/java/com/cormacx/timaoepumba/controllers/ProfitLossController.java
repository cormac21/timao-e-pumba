package com.cormacx.timaoepumba.controllers;


import com.cormacx.timaoepumba.entities.aggregate.ProfitLoss;
import com.cormacx.timaoepumba.service.ProfitLossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/profit_loss")
public class ProfitLossController {

    private final ProfitLossService profitLossService;

    @Autowired
    public ProfitLossController(ProfitLossService profitLossService) {
        this.profitLossService = profitLossService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<ProfitLoss>> getAllAccountsProfitLoss(@PathVariable Long accountId) {
        return ResponseEntity.ok(profitLossService.getAllAccountsProfitLosses(accountId));
    }

}
