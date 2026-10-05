package com.restaurant.order_service.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItem {

    private UUID id;

    private UUID productId;

    private int quantity;

    private BigDecimal unitPrice;

    private BigDecimal subtotal;

    public OrderItem(
            UUID id,
            UUID productId,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal
    ) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public static OrderItem create(
            UUID productId,
            int quantity,
            BigDecimal unitPrice
    ) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero"
            );
        }

        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El precio unitario no puede ser negativo"
            );
        }

        BigDecimal subtotal = unitPrice.multiply(
                BigDecimal.valueOf(quantity)
        );

        return new OrderItem(
                null,
                productId,
                quantity,
                unitPrice,
                subtotal
        );
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}