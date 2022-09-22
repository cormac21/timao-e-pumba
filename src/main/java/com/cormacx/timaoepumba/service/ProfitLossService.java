package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.repositories.ProfitLossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfitLossService {

    private final ProfitLossRepository profitLossRepository;

    @Autowired
    public ProfitLossService(ProfitLossRepository profitLossRepository) {
        this.profitLossRepository = profitLossRepository;
    }



}
