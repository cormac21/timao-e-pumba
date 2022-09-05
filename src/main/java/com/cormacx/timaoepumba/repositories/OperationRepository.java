package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.operation.Operation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface OperationRepository extends PagingAndSortingRepository<Operation, BigInteger> {

}
