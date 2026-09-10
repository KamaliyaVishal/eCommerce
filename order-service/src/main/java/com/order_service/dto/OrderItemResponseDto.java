package com.order_service.dto;

import lombok.Data;

@Data
public class OrderItemResponseDto {
    private Long id;
    private Long productId;
    private Integer quantity;
}
