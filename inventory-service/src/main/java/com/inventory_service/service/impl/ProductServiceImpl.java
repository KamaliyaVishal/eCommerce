package com.inventory_service.service.impl;

import com.inventory_service.dto.ProductRequestDto;
import com.inventory_service.dto.ProductResponseDto;
import com.inventory_service.entity.Product;
import com.inventory_service.repository.ProductRepository;
import com.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductResponseDto> getAllInventory() {
        log.info("Fetching all inventory items");

        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(ProductResponseDto::fromEntity)
                .toList();
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        log.info("Fetching Product with item ID: {}", id);

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found"));
        return ProductResponseDto.fromEntity(product);
    }

    @Override
    public ProductResponseDto save(ProductRequestDto productRequestDto) {
        log.info("Creating inventory items");

        // 1. Convert incoming DTO to Entity via standard instance method
        Product productEntity = productRequestDto.toEntity();

        Product savedProduct = productRepository.save(productEntity);

        // 2. Convert database result back to explicit Response DTO using factory method
        return ProductResponseDto.fromEntity(savedProduct);
    }
}
