package com.peer.orders.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequestDto {

    @NotNull
    private String customerId;

    @NotEmpty
    private List<OrderItemDto> items;

    public static class OrderItemDto {
        @NotNull
        public String productId;
        @NotNull
        public String name;
        @NotNull
        public Integer quantity;
        @NotNull
        public BigDecimal price;
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public List<OrderItemDto> getItems() { return items; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
}
