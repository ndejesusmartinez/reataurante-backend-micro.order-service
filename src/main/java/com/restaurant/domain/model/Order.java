package com.restaurant.order_service.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private UUID id;

    private UUID restaurantId;

    private UUID customerId;

    private OrderStatus status;

    private OrderType orderType;

    private BigDecimal total;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<OrderItem> items;

    public Order(
            UUID id,
            UUID restaurantId,
            UUID customerId,
            OrderStatus status,
            OrderType orderType,
            BigDecimal total,
            List<OrderItem> items,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.customerId = customerId;
        this.status = status;
        this.orderType = orderType;
        this.total = total;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Order create(
            UUID restaurantId,
            UUID customerId,
            OrderType orderType
    ) {

        LocalDateTime now = LocalDateTime.now();

        return new Order(
                null,
                restaurantId,
                customerId,
                OrderStatus.PENDING,
                orderType,
                BigDecimal.ZERO,
                new ArrayList<>(),
                now,
                now
        );
    }

    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Solo un pedido pendiente puede ser confirmado"
            );
        }

        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {

        if (this.status == OrderStatus.COMPLETED) {
            throw new IllegalStateException(
                    "No se puede cancelar un pedido completado"
            );
        }

        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {

        this.items.add(item);

        recalculateTotal();

        this.updatedAt = LocalDateTime.now();
    }

    private void recalculateTotal() {

        this.total = this.items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() {
        return id;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }
}