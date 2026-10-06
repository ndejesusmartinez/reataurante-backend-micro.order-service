package com.restaurant.order_service.presentation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderItemRequest(

        @NotNull(message = "El producto es obligatorio")
        UUID productId,

        @Min(value = 1, message = "La cantidad debe ser mayor que cero")
        int quantity,

        @NotNull(message = "El precio unitario es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
        BigDecimal unitPrice
) {}