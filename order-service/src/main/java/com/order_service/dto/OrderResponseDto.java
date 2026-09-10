package com.order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private List<OrderItemRsponseDto> itemItems;
    private Double totalPrice;
}
