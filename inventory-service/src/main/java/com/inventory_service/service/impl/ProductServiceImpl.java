package com.inventory_service.service.impl;

import com.inventory_service.dto.ProductResponseDto;
import com.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import repository.ProductRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductResponseDto> getAllInvetory() {
        return List.of();
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        return null;
    }
}
