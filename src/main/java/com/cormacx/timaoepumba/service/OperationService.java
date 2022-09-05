package com.cormacx.timaoepumba.service;

import com.cormacx.timaoepumba.entities.operation.Operation;
import com.cormacx.timaoepumba.repositories.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OperationService {

    private OperationRepository operationRepository;

    private AccountService accountService;

    @Autowired
    public OperationService(OperationRepository operationRepository, AccountService accountService) {
        this.operationRepository = operationRepository;
        this.accountService = accountService;
    }


    public Optional<Operation> getOperationById(BigInteger id) {
        return operationRepository.findById(id);

    }

    public Optional<Operation> createNewOperation(Operation op) {
        if (isValidOperation(op)){
            return Optional.of(operationRepository.save(op));
        }
        return Optional.empty();
    }

    private boolean isValidOperation(Operation op) {


        return false;
    }

    public List<Operation> getAllOperations(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Operation> pageOfOperations = operationRepository.findAll(paging);

        if( pageOfOperations.hasContent()){
            return pageOfOperations.stream().toList();
        } else {
            return new ArrayList<Operation>();
        }
    }
}
