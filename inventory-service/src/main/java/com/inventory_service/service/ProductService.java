package com.inventory_service.service;

import com.inventory_service.dto.ProductRequestDto;
import com.inventory_service.dto.ProductResponseDto;
import com.inventory_service.entity.Product;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAllInventory();

    ProductResponseDto getProductById(Long id);

    ProductResponseDto save(ProductRequestDto productRequestDto);
}
