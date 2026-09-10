package com.inventory_service.dto;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {
    private String title;
    private Double price;
    private Integer stock;
}
