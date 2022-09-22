package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.aggregate.ProfitLoss;
import com.cormacx.timaoepumba.repositories.ProfitLossRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProfitLossServiceTest {

    @InjectMocks
    private ProfitLossService profitLossService;

    @Mock
    private ProfitLossRepository profitLossRepository;

    @Test
    public void canSaveNewProfitLoss() {
        ProfitLoss profitLoss = new ProfitLoss();

    }


}
