package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.Account;
import com.cormacx.timaoepumba.repositories.ProfitLossRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProfitLossServiceTest {

    @InjectMocks
    private ProfitLossService profitLossService;

    @Mock
    private ProfitLossRepository profitLossRepository;


    private String randomUserUUID = UUID.randomUUID().toString();
    private boolean setup;
    private Account account;

    @BeforeEach
    public void setup() {
        if(!setup) {
            account = new Account(randomUserUUID, 78956.75D, true);

        }
    }

}
