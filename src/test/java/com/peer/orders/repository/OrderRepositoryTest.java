package com.peer.orders.repository;


import com.peer.orders.model.OrderEntity;
import com.peer.orders.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository repository;

    @Test
    void testSaveAndFind() {
        OrderEntity order = new OrderEntity();
        order.setCustomerId("CUST100");
        order.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.valueOf(200));

        repository.save(order);

        OrderEntity found = repository.findById(order.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getCustomerId()).isEqualTo("CUST100");
    }

    @Test
    void testFindByStatus() {
        OrderEntity order = new OrderEntity();
        order.setCustomerId("A1");
        order.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.PROCESSING);
        order.setTotalAmount(BigDecimal.valueOf(50));

        repository.save(order);

        List<OrderEntity> results = repository.findByStatus(OrderStatus.PROCESSING);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCustomerId()).isEqualTo("A1");
    }
}
