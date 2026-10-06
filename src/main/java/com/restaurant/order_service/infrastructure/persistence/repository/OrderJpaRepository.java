package com.restaurant.order_service.infrastructure.persistence.repository;

import com.restaurant.order_service.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    @Override
    @EntityGraph(attributePaths = "items")
    List<OrderEntity> findAll();

    @Override
    @EntityGraph(attributePaths = "items")
    java.util.Optional<OrderEntity> findById(UUID id);

    @EntityGraph(attributePaths = "items")
    List<OrderEntity> findByCustomerId(UUID customerId);

    @EntityGraph(attributePaths = "items")
    List<OrderEntity> findByRestaurantId(UUID restaurantId);
}