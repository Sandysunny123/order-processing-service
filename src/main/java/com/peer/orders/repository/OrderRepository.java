package com.peer.orders.repository;

import com.peer.orders.model.OrderEntity;
import com.peer.orders.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus status);
    List<OrderEntity> findByStatusIn(List<OrderStatus> statuses);
}
