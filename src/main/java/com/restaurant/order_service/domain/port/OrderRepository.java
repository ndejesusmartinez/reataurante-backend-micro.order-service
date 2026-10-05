package com.restaurant.order_service.domain.port;

import com.restaurant.order_service.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);

    List<Order> findAll();

    Optional<Order> findById(UUID id);

    List<Order> findByCustomerId(UUID customerId);

    List<Order> findByRestaurantId(UUID restaurantId);

    void deleteById(UUID id);
}