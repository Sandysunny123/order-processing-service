package com.peer.orders.dto;

import com.peer.orders.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderResponse {
    public Long id;
    public String customerId;
    public OrderStatus status;
    public Instant createdAt;
    public BigDecimal totalAmount;
    public List<OrderItemResponse> items;

    public static class OrderItemResponse {
        public Long id;
        public String productId;
        public String name;
        public int quantity;
        public BigDecimal price;
    }
}
