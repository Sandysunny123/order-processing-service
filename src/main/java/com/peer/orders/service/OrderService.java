package com.peer.orders.service;

import com.peer.orders.dto.OrderRequestDto;
import com.peer.orders.exception.InvalidStatusException;
import com.peer.orders.exception.OrderNotFoundException;
import com.peer.orders.model.OrderEntity;
import com.peer.orders.model.OrderItem;
import com.peer.orders.model.OrderStatus;
import com.peer.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public OrderEntity createOrder(OrderRequestDto req) {
        OrderEntity order = new OrderEntity();
        order.setCustomerId(req.getCustomerId());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(Instant.now());

        BigDecimal total = BigDecimal.ZERO;
        for (OrderRequestDto.OrderItemDto it : req.getItems()) {
            OrderItem item = new OrderItem(it.productId, it.name, it.quantity, it.price);
            order.addItem(item);
            BigDecimal itemTotal = it.price.multiply(BigDecimal.valueOf(it.quantity));
            total = total.add(itemTotal);
        }
        order.setTotalAmount(total);
        return repo.save(order);
    }

    public OrderEntity getOrderById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + id));
    }


    public List<OrderEntity> listOrders(Optional<OrderStatus> status) {
        return status.map(repo::findByStatus).orElseGet(repo::findAll);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        OrderEntity order = repo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " not present"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidStatusException("Only PENDING orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        repo.save(order);
    }

    @Transactional
    public void updateStatusToProcessing() {
        List<OrderEntity> pending = repo.findByStatus(OrderStatus.PENDING);

        if (pending.isEmpty()) {
            return; // no exception needed; scheduler should be silent
        }

        for (OrderEntity o : pending) {
            o.setStatus(OrderStatus.PROCESSING);
        }

        repo.saveAll(pending);
    }

    @Transactional
    public void updateStatus(Long orderId, OrderStatus newStatus) {

        if (newStatus == null) {
            throw new InvalidStatusException("Order status cannot be null");
        }

        OrderEntity order = repo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " not present"));

        order.setStatus(newStatus);
        repo.save(order);
    }
}
