package com.restaurant.order_service.application.service;

import com.restaurant.order_service.application.command.CreateOrderItemCommand;
import com.restaurant.order_service.domain.model.Order;
import com.restaurant.order_service.domain.model.OrderItem;
import com.restaurant.order_service.domain.model.OrderType;
import com.restaurant.order_service.domain.port.OrderRepository;
import org.springframework.stereotype.Service;
import com.restaurant.order_service.presentation.exception.OrderNotFoundException;
import com.restaurant.order_service.infrastructure.client.InventoryClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.UUID;

@Service
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public OrderApplicationService(
            OrderRepository orderRepository,
            InventoryClient inventoryClient
    ) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    public Order create(
        UUID restaurantId,
        UUID customerId,
        OrderType orderType,
        List<CreateOrderItemCommand> items
    ) {

        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String token = authentication.getToken().getTokenValue();

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

        // 1. Validar disponibilidad de TODOS los productos
        for (OrderItem item : order.getItems()) {

            InventoryClient.ProductAvailabilityResponse availability =
                    inventoryClient.checkAvailability(
                            item.getProductId(),
                            item.getQuantity(),
                            token
                    );

            if (!availability.available()) {
                throw new IllegalStateException(
                        "Stock insuficiente para el producto: "
                                + item.getProductId()
                );
            }
        }

        // Productos cuyo stock ya fue descontado
        List<OrderItem> discountedItems = new java.util.ArrayList<>();

        try {

            // 2. Descontar stock
            for (OrderItem item : order.getItems()) {

                inventoryClient.decreaseStock(
                        item.getProductId(),
                        item.getQuantity(),
                        token
                );

                discountedItems.add(item);
            }

            // 3. Guardar pedido
            return orderRepository.save(order);

        } catch (Exception exception) {

            // 4. Compensar los descuentos realizados
            for (OrderItem item : discountedItems) {

                try {

                    inventoryClient.increaseStock(
                            item.getProductId(),
                            item.getQuantity(),
                            token
                    );

                } catch (Exception compensationException) {

                    // No ocultamos el error original.
                    // La compensación fallida deberá registrarse
                    // posteriormente mediante logs/eventos.
                }
            }

            throw new IllegalStateException(
                    "No fue posible crear el pedido. "
                            + "Los cambios de inventario fueron revertidos.",
                    exception
            );
        }
    }

    public List<Order> findAll() {

        return orderRepository.findAll();
    }

    public Order findById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Pedido no encontrado: " + id)
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