package com.order_service.dto;

import lombok.Data;

@Data
public class OrderItemRsponseDto {
    private Long id;
    private Long productId;
    private Integer quantity;
}
