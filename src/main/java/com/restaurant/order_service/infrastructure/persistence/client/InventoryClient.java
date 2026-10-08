package com.restaurant.order_service.infrastructure.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import java.util.UUID;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8085")
                .build();
    }

    public ProductAvailabilityResponse checkAvailability(
            UUID productId,
            int quantity,
            String token
    ) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/products/{id}/availability")
                        .queryParam("quantity", quantity)
                        .build(productId))
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(ProductAvailabilityResponse.class);
    }

    public ProductStockResponse decreaseStock(
        UUID productId,
        int quantity,
        String token
        ) {
        return restClient.patch()
                .uri("/api/products/{id}/stock/decrease", productId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new StockRequest(quantity))
                .retrieve()
                .body(ProductStockResponse.class);
        }

    public record ProductAvailabilityResponse(
            UUID productId,
            boolean available,
            int stock
    ) {
    }

    public record StockRequest(
        int quantity
        ) {
        }
}