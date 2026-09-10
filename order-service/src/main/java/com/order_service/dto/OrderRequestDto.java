package com.order_service.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderRequestDto(
    @NotEmpty(message = "An order must contain at least one item")
    List<OrderItemRequestDto> items
) {}
