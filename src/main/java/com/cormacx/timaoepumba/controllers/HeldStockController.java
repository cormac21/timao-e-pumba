package com.cormacx.timaoepumba.controllers;

import com.cormacx.timaoepumba.entities.account.HeldStock;
import com.cormacx.timaoepumba.service.HeldStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/held_stocks")
public class HeldStockController {

    private final HeldStockService heldStockService;

    public HeldStockController(HeldStockService heldStockService) {
        this.heldStockService = heldStockService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<HeldStock>> getAllHeldStocksForAccount(@PathVariable Long id) {
        return ResponseEntity.ok(heldStockService.getAllHeldStocksOnAccount(id));
    }


}
