package com.restaurant.order_service.infrastructure.persistence.adapter;

import com.restaurant.order_service.domain.model.Order;
import com.restaurant.order_service.domain.model.OrderItem;
import com.restaurant.order_service.domain.port.OrderRepository;
import com.restaurant.order_service.infrastructure.persistence.entity.OrderEntity;
import com.restaurant.order_service.infrastructure.persistence.entity.OrderItemEntity;
import com.restaurant.order_service.infrastructure.persistence.repository.OrderJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(
            OrderJpaRepository orderJpaRepository
    ) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order order) {

        OrderEntity entity = toEntity(order);

        OrderEntity savedEntity =
                orderJpaRepository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public List<Order> findAll() {

        return orderJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Order> findById(UUID id) {

        return orderJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(UUID customerId) {

        return orderJpaRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByRestaurantId(UUID restaurantId) {

        return orderJpaRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {

        orderJpaRepository.deleteById(id);
    }

    private OrderEntity toEntity(Order order) {

        OrderEntity entity = new OrderEntity(
                order.getId(),
                order.getRestaurantId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getOrderType(),
                order.getTotal(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );

        List<OrderItemEntity> itemEntities = order.getItems()
                .stream()
                .map(item -> toItemEntity(item, entity))
                .toList();

        entity.setItems(itemEntities);

        return entity;
    }

    private OrderItemEntity toItemEntity(
            OrderItem item,
            OrderEntity orderEntity
    ) {

        OrderItemEntity entity = new OrderItemEntity(
                item.getId(),
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );

        entity.setOrder(orderEntity);

        return entity;
    }

    private Order toDomain(OrderEntity entity) {

        List<OrderItem> items = entity.getItems()
                .stream()
                .map(this::toItemDomain)
                .toList();

        return new Order(
                entity.getId(),
                entity.getRestaurantId(),
                entity.getCustomerId(),
                entity.getStatus(),
                entity.getOrderType(),
                entity.getTotal(),
                items,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private OrderItem toItemDomain(OrderItemEntity entity) {

        return new OrderItem(
                entity.getId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getSubtotal()
        );
    }
}