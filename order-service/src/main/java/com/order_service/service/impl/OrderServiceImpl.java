package com.order_service.service.impl;

import com.order_service.dto.OrderResponseDto;
import com.order_service.entity.Order;
import com.order_service.repository.OrderRepository;
import com.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<OrderResponseDto> getAllOrders() {
        log.info("Fetching the Orders");
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream()
                .map(order -> modelMapper.map(order, OrderResponseDto.class))
                .toList();
    }

    @Override
    public OrderResponseDto getOrderByID(Long id) {
        log.info("Fetching the Order with Id: {}", id);
        return modelMapper.map(orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found")), OrderResponseDto.class);
    }
}
