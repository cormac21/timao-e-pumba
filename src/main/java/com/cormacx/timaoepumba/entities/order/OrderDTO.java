package com.cormacx.timaoepumba.entities.order;

import com.cormacx.timaoepumba.entities.account.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
@NoArgsConstructor
public class OrderDTO {

    private String userUUID;
    private String type;
    @Min(value = 1L)
    private Integer quantity;
    private String ticker;
    private Double unitPrice;
    private Date createdOn;

    public static Order toEntity(OrderDTO dto, Account account) throws InvalidOrderTypeException {
        Order order = new Order();
        order.setUserUUID(dto.getUserUUID());
        if(dto.getType().equalsIgnoreCase("c")) {
            order.setType(OrderType.BUY);
        } else if(dto.getType().equalsIgnoreCase("v")) {
            order.setType(OrderType.SELL);
        } else {
            throw new InvalidOrderTypeException();
        }
        order.setTicker(dto.getTicker());
        order.setQuantity(dto.getQuantity());
        order.setUnitPrice(dto.getUnitPrice());
        order.setCreatedOn(dto.getCreatedOn());
        order.setAccount(account);
        order.setTotalPrice(new BigDecimal(dto.getQuantity() * dto.getUnitPrice())
                .setScale(2, RoundingMode.HALF_UP).doubleValue());
        return order;
    }

}
