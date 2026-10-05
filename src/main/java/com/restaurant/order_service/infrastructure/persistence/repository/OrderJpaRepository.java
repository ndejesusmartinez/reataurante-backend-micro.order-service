package com.restaurant.order_service.infrastructure.persistence.repository;

import com.restaurant.order_service.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findByCustomerId(UUID customerId);

    List<OrderEntity> findByRestaurantId(UUID restaurantId);
}