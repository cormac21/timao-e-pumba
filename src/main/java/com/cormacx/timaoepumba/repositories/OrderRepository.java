package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.order.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, BigInteger> {

}
