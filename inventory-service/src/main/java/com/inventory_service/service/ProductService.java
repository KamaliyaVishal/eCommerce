package com.inventory_service.service;

import com.inventory_service.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAllInvetory();

    ProductResponseDto getProductById(Long id);
}
