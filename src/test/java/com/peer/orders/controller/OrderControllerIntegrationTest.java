package com.peer.orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peer.orders.dto.OrderRequestDto;
import com.peer.orders.model.OrderStatus;
import com.peer.orders.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository repo;

    @Autowired
    private ObjectMapper mapper;

    private OrderRequestDto validRequest;

    @BeforeEach
    void setup() {
        repo.deleteAll();

        validRequest = new OrderRequestDto();
        validRequest.setCustomerId("CUST-001");

        OrderRequestDto.OrderItemDto item = new OrderRequestDto.OrderItemDto();
        item.productId = "P1";
        item.name = "Item One";
        item.quantity = 2;
        item.price = new BigDecimal("50");

        validRequest.setItems(List.of(item));
    }


    @Test
    void testCreateOrder() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value("CUST-001"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    void testGetOrder() throws Exception {
        // insert
        Long id = repo.save(TestData.sampleOrder()).getId();

        mockMvc.perform(get("/api/v1/orders/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetOrderNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/orders/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Order not found")));
    }


    @Test
    void testCancelOrder() throws Exception {
        Long id = repo.save(TestData.sampleOrder()).getId();

        mockMvc.perform(delete("/api/v1/orders/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCancelOrderAlreadyProcessing() throws Exception {
        // create a PROCESSING order
        var o = TestData.sampleOrder();
        o.setStatus(OrderStatus.PROCESSING);
        Long id = repo.save(o).getId();

        mockMvc.perform(delete("/api/v1/orders/" + id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Only PENDING orders can be cancelled"));
    }


    @Test
    void testUpdateStatus() throws Exception {
        Long id = repo.save(TestData.sampleOrder()).getId();

        mockMvc.perform(patch("/api/v1/orders/" + id + "/status")
                        .param("value", "SHIPPED"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/orders/" + id))
                .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    void testUpdateStatusInvalid() throws Exception {
        Long id = repo.save(TestData.sampleOrder()).getId();

        mockMvc.perform(patch("/api/v1/orders/" + id + "/status")
                        .param("value", "INVALID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testListOrders() throws Exception {
        repo.save(TestData.sampleOrder());

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testListOrdersByStatus() throws Exception {
        var o = TestData.sampleOrder();
        o.setStatus(OrderStatus.SHIPPED);
        repo.save(o);

        mockMvc.perform(get("/api/v1/orders?status=SHIPPED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("SHIPPED"));
    }
}
