package com.restaurant.order_service.domain.model;

public enum OrderStatus {

    PENDING,
    CONFIRMED,
    PREPARING,
    READY,
    DELIVERING,
    COMPLETED,
    CANCELLED
}