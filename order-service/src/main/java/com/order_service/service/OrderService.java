package com.order_service.service;

import com.order_service.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto getOrderByID(Long id);

}
