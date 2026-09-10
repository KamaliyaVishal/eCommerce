package com.inventory_service.controller;

import com.inventory_service.dto.ProductResponseDto;
import com.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllInventory() {
        return ResponseEntity.ok(productService.getAllInvetory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

}
