package com.peer.orders.controller;

import com.peer.orders.dto.OrderRequestDto;
import com.peer.orders.dto.OrderResponse;
import com.peer.orders.exception.InvalidStatusException;
import com.peer.orders.mapper.OrderMapper;
import com.peer.orders.model.OrderEntity;
import com.peer.orders.model.OrderStatus;
import com.peer.orders.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequestDto req) {
        OrderEntity saved = orderService.createOrder(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderMapper.toResponse(saved));
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderEntity order = orderService.getOrderById(id);
        return ResponseEntity.ok(OrderMapper.toResponse(order));
    }


    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestParam(required = false) OrderStatus status) {
        List<OrderEntity> list = orderService.listOrders(Optional.ofNullable(status));
        List<OrderResponse> res = list.stream().map(OrderMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }



    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String value
    ) {
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidStatusException("Invalid status value: " + value);
        }

        orderService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }



}
