package com.inventory_service.service.impl;

import com.inventory_service.dto.ProductResponseDto;
import com.inventory_service.entity.Product;
import com.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.inventory_service.repository.ProductRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductResponseDto> getAllInventory() {
        log.info("Fetching all inventory items");
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        log.info("Fetching Product with iteam ID: {}", id);
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found"));
        return modelMapper.map(product, ProductResponseDto.class);
    }
}
