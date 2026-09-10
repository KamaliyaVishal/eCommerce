package com.order_service.service;

import com.order_service.dto.OrderRequestDto;
import com.order_service.dto.OrderResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto getOrderByID(Long id);

    OrderResponseDto createOrder(@Valid OrderRequestDto request);
}

