package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.DepositWithdrawal;
import com.cormacx.timaoepumba.repositories.DepositWithdrawalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DepositWithdrawalServiceTest {

    @InjectMocks
    private DepositWithdrawalService depositWithdrawalService;

    @Mock
    private DepositWithdrawalRepository depositWithdrawalRepository;





}
