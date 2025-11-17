package com.peer.orders.scheduling;

import com.peer.orders.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OrderStatusScheduler {

    private final OrderService orderService;

    public OrderStatusScheduler(OrderService orderService) {
        this.orderService = orderService;
    }


    @Scheduled(cron = "0 */5 * * * *")
    public void promotePendingOrders() {

        System.out.println("Scheduler triggered at: " + Instant.now());
        orderService.updateStatusToProcessing();
    }
}
