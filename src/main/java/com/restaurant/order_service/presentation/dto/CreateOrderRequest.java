package com.restaurant.order_service.presentation.dto;

import com.restaurant.order_service.domain.model.OrderType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(

        @NotNull(message = "El restaurante es obligatorio")
        UUID restaurantId,

        @NotNull(message = "El cliente es obligatorio")
        UUID customerId,

        @NotNull(message = "El tipo de pedido es obligatorio")
        OrderType orderType,

        @NotEmpty(message = "El pedido debe tener al menos un producto")
        @Valid
        List<CreateOrderItemRequest> items
) {}