package com.inventory_service.dto;

import com.inventory_service.entity.Product;

public record ProductResponseDto(
        Long id,
        String title,
        Double price,
        Integer stockQuantity
) {
    // Instantiate a Record using the Entity's state
    public static ProductResponseDto fromEntity(Product entity) {
        if (entity == null) {
            return null;
        }
        return new ProductResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getPrice(),
                entity.getStockQuantity()
        );
    }
}