package com.restaurant.order_service.infrastructure.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductStockResponse(
        UUID id,
        UUID restaurantId,
        String name,
        String description,
        BigDecimal price,
        int stock,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}