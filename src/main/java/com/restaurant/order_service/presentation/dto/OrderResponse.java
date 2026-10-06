package com.restaurant.order_service.presentation.dto;

import com.restaurant.order_service.domain.model.Order;
import com.restaurant.order_service.domain.model.OrderItem;
import com.restaurant.order_service.domain.model.OrderStatus;
import com.restaurant.order_service.domain.model.OrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID restaurantId,
        UUID customerId,
        OrderStatus status,
        OrderType orderType,
        BigDecimal total,
        List<OrderItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static OrderResponse fromDomain(Order order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(OrderItemResponse::fromDomain)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getRestaurantId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getOrderType(),
                order.getTotal(),
                items,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}