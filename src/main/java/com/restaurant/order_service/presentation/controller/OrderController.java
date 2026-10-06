package com.restaurant.order_service.presentation.controller;

import com.restaurant.order_service.application.command.CreateOrderItemCommand;
import com.restaurant.order_service.application.service.OrderApplicationService;
import com.restaurant.order_service.domain.model.Order;
import com.restaurant.order_service.presentation.dto.CreateOrderRequest;
import com.restaurant.order_service.presentation.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {

        List<CreateOrderItemCommand> items = request.items()
                .stream()
                .map(item -> new CreateOrderItemCommand(
                        item.productId(),
                        item.quantity(),
                        item.unitPrice()
                ))
                .toList();

        Order order = orderApplicationService.create(
                request.restaurantId(),
                request.customerId(),
                request.orderType(),
                items
        );

        return OrderResponse.fromDomain(order);
    }

    @GetMapping
    public List<OrderResponse> findAll() {
        return orderApplicationService.findAll()
                .stream()
                .map(OrderResponse::fromDomain)
                .toList();
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable UUID id) {
        return OrderResponse.fromDomain(
                orderApplicationService.findById(id)
        );
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderResponse> findByCustomerId(
            @PathVariable UUID customerId
    ) {
        return orderApplicationService.findByCustomerId(customerId)
                .stream()
                .map(OrderResponse::fromDomain)
                .toList();
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<OrderResponse> findByRestaurantId(
            @PathVariable UUID restaurantId
    ) {
        return orderApplicationService.findByRestaurantId(restaurantId)
                .stream()
                .map(OrderResponse::fromDomain)
                .toList();
    }

    @PatchMapping("/{id}/confirm")
    public OrderResponse confirm(@PathVariable UUID id) {
        return OrderResponse.fromDomain(
                orderApplicationService.confirm(id)
        );
    }

    @PatchMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable UUID id) {
        return OrderResponse.fromDomain(
                orderApplicationService.cancel(id)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        orderApplicationService.delete(id);
    }
}