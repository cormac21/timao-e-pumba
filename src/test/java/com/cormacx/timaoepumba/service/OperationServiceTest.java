package com.cormacx.timaoepumba.service;


import com.cormacx.timaoepumba.repositories.AccountRepository;
import com.cormacx.timaoepumba.repositories.OperationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTest {

    @InjectMocks
    private OperationService operationService;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void isThereEnoughBalanceForOperationTest(){
        boolean result = operationService.isThereEnoughBalanceOnAccount(1500.00D, 100, 20.5D);

        assertFalse(result);
    }

    @Test
    public void theresNotEnoughBalanceForNewOperationTest(){
        boolean result = operationService.isThereEnoughBalanceOnAccount(3500.99D, 100, 20.5D);

        assertTrue(result);
    }

}
