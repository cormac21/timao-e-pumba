package com.cormacx.timaoepumba.controllers;

import com.cormacx.timaoepumba.entities.Operation;
import com.cormacx.timaoepumba.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController("/operations")
public class OperationController {

    private OperationService operationService;

    @Autowired
    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }


    @GetMapping("/{id}")
    public Operation getSingleOperation(@PathVariable BigInteger id) {
        Optional<Operation> operationOp = operationService.getOperationById(id);
        if (operationOp.isPresent()){
            return operationOp.get();
        } else {
            return null;
        }
    }

    @PostMapping
    public Operation createNewOperation(@RequestBody Operation op) {
        Optional<Operation> operationOp = operationService.createNewOperation(op);
        if (operationOp.isPresent()) {
            return operationOp.get();
        } else {
            return null;
        }
    }

    @GetMapping
    public ResponseEntity<List<Operation>> getAllOperations( @RequestParam Integer pageNumber,
                                                             @RequestParam Integer pageSize,
                                                             @RequestParam String sortBy) {
        List<Operation> operations = operationService.getAllOperations(pageNumber, pageSize, sortBy);

        return new ResponseEntity<List<Operation>>(operations, new HttpHeaders(), HttpStatus.OK);
    }

}
