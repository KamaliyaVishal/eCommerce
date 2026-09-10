package com.order_service.service.impl;

import com.order_service.client.InventoryClient;
import com.order_service.dto.OrderRequestDto;
import com.order_service.dto.OrderResponseDto;
import com.order_service.entity.Order;
import com.order_service.entity.OrderItem;
import com.order_service.entity.enums.OrderStatus;
import com.order_service.repository.OrderRepository;
import com.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    @Override
    public List<OrderResponseDto> getAllOrders() {
        log.info("Fetching the Orders");
        List<Order> orderList = orderRepository.findAll();
        return orderList.stream()
                .map(OrderResponseDto::fromEntity)
                .toList();
    }

    @Override
    public OrderResponseDto getOrderByID(Long id) {
        log.info("Fetching the Order with Id: {}", id);

        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return OrderResponseDto.fromEntity(order);
    }

    @Override
    public OrderResponseDto createOrder(OrderRequestDto request) {
        log.info("Initiating order creation process");

        // 1. Initialize parent Order entity
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PLACED);

        double computedTotalPrice = 0.0;

        // 2. Iterate through incoming items, contact Inventory, and build snapshots
        for (var itemDto : request.items()) {
            log.info("Requesting stock reduction for product: {}, quantity: {}", itemDto.productId(), itemDto.quantity());

            // Feign Call: Reduces stock in inventory-service and grabs the official price snapshot
            Double snapshotPrice = inventoryClient.reduceStock(itemDto.productId(), itemDto.quantity());

            // Build child entity row
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemDto.productId());
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setPrice(snapshotPrice);

            // Link bidirectionally using the entity helper method
            order.addOrderItem(orderItem);

            // Accumulate calculations
            computedTotalPrice += (snapshotPrice * itemDto.quantity());
        }

        // 3. Complete parental details and commit the entire tree via CascadeType.ALL
        order.setTotalPrice(computedTotalPrice);
        Order savedOrder = orderRepository.save(order);

        log.info("Order successfully created with ID: {}", savedOrder.getId());

        // 4. Return pure Java DTO mapping
        return OrderResponseDto.fromEntity(savedOrder);
    }
}
