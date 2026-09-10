package com.inventory_service.controller;

import com.inventory_service.dto.ProductRequestDto;
import com.inventory_service.dto.ProductResponseDto;
import com.inventory_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllInventory() {
        return ResponseEntity.ok(productService.getAllInventory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto request) {
        return ResponseEntity.ok(productService.save(request));
    }

}
