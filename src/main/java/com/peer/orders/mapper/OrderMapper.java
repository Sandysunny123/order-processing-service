package com.peer.orders.mapper;

import com.peer.orders.dto.OrderResponse;
import com.peer.orders.model.OrderEntity;
import com.peer.orders.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public static OrderResponse toResponse(OrderEntity entity) {
        if (entity == null) return null;

        OrderResponse response = new OrderResponse();
        response.id = entity.getId();
        response.customerId = entity.getCustomerId();
        response.status = entity.getStatus();
        response.totalAmount = entity.getTotalAmount();
        response.createdAt = entity.getCreatedAt();

        response.items = entity.getItems()
                .stream()
                .map(OrderMapper::mapItem)
                .collect(Collectors.toList());

        return response;
    }


    private static OrderResponse.OrderItemResponse mapItem(OrderItem item) {
        OrderResponse.OrderItemResponse dto = new OrderResponse.OrderItemResponse();
        dto.id = item.getId();
        dto.productId = item.getProductId();
        dto.name = item.getName();
        dto.quantity = item.getQuantity();
        dto.price = item.getPrice();
        return dto;
    }
}
