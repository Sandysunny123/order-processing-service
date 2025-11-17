package com.peer.orders.controller;

import com.peer.orders.model.OrderEntity;
import com.peer.orders.model.OrderItem;
import com.peer.orders.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class TestData {

    public static OrderEntity sampleOrder() {

        OrderEntity o = new OrderEntity();
        o.setCustomerId("CUST-TEST");
        o.setStatus(OrderStatus.PENDING);
        o.setCreatedAt(Instant.now());

        OrderItem item = new OrderItem("P1", "Item Test", 1, new BigDecimal("10"));
        o.setItems(List.of(item));
        item.setOrder(o);

        o.setTotalAmount(new BigDecimal("10"));

        return o;
    }
}
