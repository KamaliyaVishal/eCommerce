package com.inventory_service.dto;

import lombok.Data;

@Data
public class ProductResponseDto {
    private String title;
    private Double price;
    private Integer stock;
}
