package com.restaurant.order_service.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderItemCommand(
        UUID productId,
        int quantity,
        BigDecimal unitPrice
) {
}