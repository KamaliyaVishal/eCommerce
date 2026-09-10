package com.inventory_service.dto;

import com.inventory_service.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequestDto(
        @NotBlank(message = "Title cannot be empty")
        String title,

        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price must be a positive value")
        Double price,

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stockQuantity
) {
    // Convert incoming request payload to a fresh Entity object
    public Product toEntity() {
        Product product = new Product();
        product.setTitle(this.title);
        product.setPrice(this.price);
        product.setStockQuantity(this.stockQuantity);
        return product;
    }
}
