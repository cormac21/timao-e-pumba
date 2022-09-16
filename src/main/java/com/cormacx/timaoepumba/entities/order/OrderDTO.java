package com.cormacx.timaoepumba.entities.order;

import com.cormacx.timaoepumba.entities.account.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderDTO {

    private String userUUID;
    private String opType;
    private Integer quantity;
    private String ticker;
    private Double unitPrice;
    private Date createdOn;

    public static Order toEntity(OrderDTO dto, Account account) throws InvalidOrderTypeException {
        Order order = new Order();
        order.setUserUUID(dto.getUserUUID());
        if(dto.getOpType().equalsIgnoreCase("c")) {
            order.setOpType(OrderType.BUY);
        } else if(dto.getOpType().equalsIgnoreCase("v")) {
            order.setOpType(OrderType.SELL);
        } else {
            throw new InvalidOrderTypeException();
        }
        order.setTicker(dto.getTicker());
        order.setQuantity(dto.getQuantity());
        order.setUnitPrice(dto.getUnitPrice());
        order.setCreatedOn(dto.getCreatedOn());
        order.setAccount(account);
        order.setTotalPrice(dto.getQuantity() * dto.getUnitPrice());
        return order;
    }

}
