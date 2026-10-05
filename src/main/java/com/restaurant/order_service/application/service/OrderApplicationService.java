package com.restaurant.order_service.application.service;

import com.restaurant.order_service.application.command.CreateOrderItemCommand;
import com.restaurant.order_service.domain.model.Order;
import com.restaurant.order_service.domain.model.OrderItem;
import com.restaurant.order_service.domain.model.OrderType;
import com.restaurant.order_service.domain.port.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderApplicationService {

    private final OrderRepository orderRepository;

    public OrderApplicationService(
            OrderRepository orderRepository
    ) {
        this.orderRepository = orderRepository;
    }

    public Order create(
            UUID restaurantId,
            UUID customerId,
            OrderType orderType,
            List<CreateOrderItemCommand> items
    ) {

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "El pedido debe tener al menos un producto"
            );
        }

        Order order = Order.create(
                restaurantId,
                customerId,
                orderType
        );

        for (CreateOrderItemCommand item : items) {

            OrderItem orderItem = OrderItem.create(
                    item.productId(),
                    item.quantity(),
                    item.unitPrice()
            );

            order.addItem(orderItem);
        }

        return orderRepository.save(order);
    }

    public List<Order> findAll() {

        return orderRepository.findAll();
    }

    public Order findById(UUID id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pedido no encontrado: " + id
                        )
                );
    }

    public List<Order> findByCustomerId(UUID customerId) {

        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> findByRestaurantId(UUID restaurantId) {

        return orderRepository.findByRestaurantId(restaurantId);
    }

    public Order confirm(UUID id) {

        Order order = findById(id);

        order.confirm();

        return orderRepository.save(order);
    }

    public Order cancel(UUID id) {

        Order order = findById(id);

        order.cancel();

        return orderRepository.save(order);
    }

    public void delete(UUID id) {

        Order order = findById(id);

        orderRepository.deleteById(order.getId());
    }
}