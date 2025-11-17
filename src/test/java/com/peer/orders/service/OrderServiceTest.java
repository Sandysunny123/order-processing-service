package com.peer.orders.service;

import com.peer.orders.dto.OrderRequestDto;
import com.peer.orders.exception.InvalidStatusException;
import com.peer.orders.model.OrderEntity;
import com.peer.orders.model.OrderStatus;
import com.peer.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class OrderServiceTest {

    @Autowired
    private OrderRepository repository;

    private OrderRequestDto createSampleOrder() {
        OrderRequestDto req = new OrderRequestDto();
        req.setCustomerId("cust101");

        OrderRequestDto.OrderItemDto item = new OrderRequestDto.OrderItemDto();
        item.productId = "P1";
        item.name = "Laptop";
        item.quantity = 2;
        item.price = BigDecimal.valueOf(500);

        req.setItems(List.of(item));
        return req;
    }

    @Test
    void testCreateOrder() {
        OrderService service = new OrderService(repository);
        OrderEntity order = service.createOrder(createSampleOrder());

        assertThat(order.getId()).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getTotalAmount()).isEqualTo("1000");
    }

    @Test
    void testCancelOrderWhenPending() {
        OrderService service = new OrderService(repository);
        OrderEntity order = service.createOrder(createSampleOrder());

        // Act
        service.cancelOrder(order.getId());  // no exception means success

        // Assert
        OrderEntity updated = repository.findById(order.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }


    @Test
    void testCancelOrderFailsIfNotPending() {
        OrderService service = new OrderService(repository);
        OrderEntity order = service.createOrder(createSampleOrder());

        // Set to PROCESSING
        service.updateStatus(order.getId(), OrderStatus.PROCESSING);

        // Act + Assert
        assertThatThrownBy(() -> service.cancelOrder(order.getId()))
                .isInstanceOf(InvalidStatusException.class)
                .hasMessage("Only PENDING orders can be cancelled");
    }

}
