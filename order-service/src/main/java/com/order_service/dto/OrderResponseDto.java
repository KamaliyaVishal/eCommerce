package com.order_service.dto;

import com.order_service.entity.Order;
import com.order_service.entity.enums.OrderStatus;

import java.util.Collections;
import java.util.List;

public record OrderResponseDto(
        Long id,
        OrderStatus orderStatus,
        Double totalPrice,
        List<OrderItemResponseDto> orderItems
) {
    // Converts entire entity tree with child collections cleanly
    public static OrderResponseDto fromEntity(Order entity) {
        if (entity == null) {
            return null;
        }

        List<OrderItemResponseDto> items = entity.getOrderItems() == null
                ? Collections.emptyList()
                : entity.getOrderItems().stream()
                .map(OrderItemResponseDto::fromEntity)
                .toList();

        return new OrderResponseDto(
                entity.getId(),
                entity.getOrderStatus(),
                entity.getTotalPrice(),
                items
        );
    }
}
