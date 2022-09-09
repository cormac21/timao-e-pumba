package com.cormacx.timaoepumba.controllers;

import com.cormacx.timaoepumba.entities.operation.Operation;
import com.cormacx.timaoepumba.entities.operation.OperationDTO;
import com.cormacx.timaoepumba.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/operations")
public class OperationController {

    private final OperationService operationService;

    @Autowired
    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }


    @GetMapping("/{id}")
    public Operation getSingleOperation(@PathVariable BigInteger id) {
        Optional<Operation> operationOp = operationService.getOperationById(id);
        return operationOp.orElse(null);
    }

    @PostMapping
    public Operation createNewOperation(@RequestBody OperationDTO op) {
        Optional<Operation> operationOp = operationService.createNewOperation(op);
        return operationOp.orElse(null);
    }

    @GetMapping
    public ResponseEntity<List<Operation>> getAllOperations( @RequestParam(defaultValue = "0") Integer pageNumber,
                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                             @RequestParam(defaultValue = "id") String sortBy) {
        List<Operation> operations = operationService.getAllOperations(pageNumber, pageSize, sortBy);

        return new ResponseEntity<>(operations, new HttpHeaders(), HttpStatus.OK);
    }

}
