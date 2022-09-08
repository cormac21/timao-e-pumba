package com.cormacx.timaoepumba.entities.operation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OperationDTO {

    private String userUUID;
    private String opType;
    private Integer quantity;
    private String ticker;
    private Double unitPrice;
    private Date createdOn;

    public static Operation toEntity(OperationDTO dto) throws InvalidOperationTypeException {
        Operation operation = new Operation();
        operation.setUserUUID(dto.getUserUUID());
        if(dto.getOpType().equalsIgnoreCase("c")) {
            operation.setOpType(OperationType.C);
        } else if(dto.getOpType().equalsIgnoreCase("v")) {
            operation.setOpType(OperationType.V);
        } else {
            throw new InvalidOperationTypeException();
        }
        operation.setTicker(dto.getTicker());
        operation.setQuantity(dto.getQuantity());
        operation.setUnitPrice(dto.getUnitPrice());
        operation.setCreatedOn(dto.getCreatedOn());
        return operation;
    }

}
