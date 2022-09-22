package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.account.DepositWithdrawal;
import com.cormacx.timaoepumba.repositories.DepositWithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositWithdrawalService {

    private final DepositWithdrawalRepository depositWithdrawalRepository;

    @Autowired
    public DepositWithdrawalService(DepositWithdrawalRepository depositWithdrawalRepository) {
        this.depositWithdrawalRepository = depositWithdrawalRepository;
    }

    public DepositWithdrawal createNewDepositWithdrawal(DepositWithdrawal depositWithdrawal) {
        return depositWithdrawalRepository.save(depositWithdrawal);
    }

}
