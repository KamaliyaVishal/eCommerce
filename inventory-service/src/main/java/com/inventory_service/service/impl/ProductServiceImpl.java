package com.inventory_service.service.impl;

import com.inventory_service.dto.ProductRequestDto;
import com.inventory_service.dto.ProductResponseDto;
import com.inventory_service.entity.Product;
import com.inventory_service.repository.ProductRepository;
import com.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public Double deductStockAndGetPrice(Long productId, Integer quantity) {
        log.info("Attempting to deduct stock for product ID: {}, quantity: {}", productId, quantity);

        // 1. Fetch the product using the Pessimistic Lock
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // 2. Validate sufficient inventory exists
        if (product.getStockQuantity() < quantity) {
            log.error("Insufficient stock for product: {}. Available: {}, Requested: {}",
                    product.getTitle(), product.getStockQuantity(), quantity);

            throw new RuntimeException("Insufficient stock for product: {}" + product.getTitle());
        }

        // 3. Deduct stock calculations
        int updatedStock = product.getStockQuantity() - quantity;
        product.setStockQuantity(updatedStock);

        // 4. Save back the updated state
        productRepository.save(product);
        log.info("Successfully deducted stock. Updated stock for product '{}' is now: {}",
                product.getTitle(), updatedStock);

        return product.getPrice();
    }
}
