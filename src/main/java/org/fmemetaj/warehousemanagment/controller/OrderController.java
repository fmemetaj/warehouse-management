package org.fmemetaj.warehousemanagment.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.fmemetaj.warehousemanagment.entity.Order;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("create")
    public ResponseEntity<Result<Order>> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody CreateOrderRequest createOrderRequest
    ) {
        return response(orderService.createOrder(user, createOrderRequest.orderItems));
    }

    @PutMapping("update")
    public ResponseEntity<Result<Order>> updateOrder(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateOrderRequest updateOrderRequest
    ) {
        return response(orderService.updateOrder(user, updateOrderRequest.orderItems));
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("{orderId}/status")
    public Order updateOrderStatus(@PathVariable Long orderId, @RequestParam Order.OrderStatus status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    private <T> ResponseEntity<Result<T>> response(Result<T> result) {
        if (result.code() != Result.Code.SUCCESS) {
            var httpStatus = result.code().toHttpStatus();
            log.info("Error: {}", result.code());

            return ResponseEntity.status(httpStatus).body(result);
        }

        return ResponseEntity.ok(result);
    }

    public record CreateOrderRequest(
            List<OrderItemRequest> orderItems
    ) {}

    public record UpdateOrderRequest(
            @NonNull Long orderId,
            List<OrderItemRequest> orderItems
    ) {}

    public record OrderItemRequest(
            Long itemId,
            int requestedQuantity
    ) {}
}
