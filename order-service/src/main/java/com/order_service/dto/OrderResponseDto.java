package com.order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDto {
    private List<OrderItemResponseDto> orderItems;
    private Double totalPrice;
}
