package com.cormacx.timaoepumba.controllers;

import com.cormacx.timaoepumba.entities.order.Order;
import com.cormacx.timaoepumba.entities.order.OrderDTO;
import com.cormacx.timaoepumba.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/{id}")
    public Order getSingleOrder(@PathVariable BigInteger id) {
        Optional<Order> OrderOp = orderService.getOrderById(id);
        return OrderOp.orElse(null);
    }

    @PostMapping
    public Order createNewOrder(@RequestBody @Valid OrderDTO op) {
        Optional<Order> OrderOp = orderService.createNewOrder(op);
        return OrderOp.orElse(null);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                    @RequestParam(defaultValue = "id") String sortBy,
                                                    @RequestParam(required = false) String userUUID) {
        List<Order> orders;
        if (userUUID != null) {
            orders = orderService.getAllOrdersFromUser(pageNumber, pageSize, sortBy, userUUID);
        } else {
            orders = orderService.getAllOrders(pageNumber, pageSize, sortBy);
        }
        return new ResponseEntity<>(orders, new HttpHeaders(), HttpStatus.OK);
    }

}
