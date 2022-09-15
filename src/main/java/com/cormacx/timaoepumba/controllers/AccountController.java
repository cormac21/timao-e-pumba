package com.cormacx.timaoepumba.controllers;


import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findAccountById(@PathVariable String id){
        Optional<Account> accountOp = accountService.findAccountByUser(id);
        if(accountOp.isPresent()){
            return ResponseEntity.ok(accountOp.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(){
        List<Account> accounts = accountService.findAllAccounts();
        return ResponseEntity.ok(accounts);
    }



}
