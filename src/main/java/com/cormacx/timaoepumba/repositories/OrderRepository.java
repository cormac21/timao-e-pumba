package com.cormacx.timaoepumba.repositories;

import com.cormacx.timaoepumba.entities.account.OperationType;
import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderType;
import jakarta.persistence.OrderBy;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ListCrudRepository<Order, BigInteger>,
        PagingAndSortingRepository<Order, BigInteger> {

    List<Order> findAll();

    @Query("select o from Order o where o.userUUID = :userUUID")
    @OrderBy("created_on desc")
    Order getLatestOrderFromUser(@Param("userUUID") String userUUID);

    @Query("select o from Order o where o.userUUID = :userUUID and o.type = :opType")
    @OrderBy("created_on desc")
    Order findOneLatestOrderFromUserAndOpType(@Param("userUUID") String userUUID, @Param("opType") OperationType opType);

    //This one worked
    Order findFirstByUserUUIDAndTickerAndTypeOrderByCreatedOnDesc(String userUUID, String ticker, OrderType type);

    @Query("select o from Order o where o.userUUID = :userUUID and o.type = 0 and o.ticker = :ticker")
    @OrderBy("created_on desc")
    Order getLatestBuyOrderFromUserAndTicker(@Param("userUUID") String userUUID, @Param("ticker") String ticker);

    @Query("select o from Order o where o.userUUID = :userUUID and o.type = 1 and o.ticker = :ticker")
    @OrderBy("created_on desc")
    Order getLatestSellOrderFromUserAndTicker(@Param("userUUID") String userUUID, @Param("ticker") String ticker);

    List<Order> findAllByUserUUID(String userUUID, Pageable pageable);

}
