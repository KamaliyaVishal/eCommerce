package com.order_service.dto;

import com.order_service.entity.OrderItem;

public record OrderItemResponseDto(
        Long id,
        Long productId,
        Integer quantity,
        Double price
) {
    // Maps database order rows cleanly to immutable records
    public static OrderItemResponseDto fromEntity(OrderItem entity) {
        if (entity == null) {
            return null;
        }
        return new OrderItemResponseDto(
                entity.getId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getPrice()
        );
    }
}
