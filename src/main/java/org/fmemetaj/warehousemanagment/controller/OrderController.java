package org.fmemetaj.warehousemanagment.controller;

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

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("create")
    public ResponseEntity<Result<Order>> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody CreateOrderRequest createOrderRequest
    ) {
        return Result.response(orderService.createOrder(user, createOrderRequest.orderItems));
    }

    @PutMapping("update")
    public ResponseEntity<Result<Order>> updateOrder(
            @AuthenticationPrincipal User user,
            @RequestBody Order updateOrderRequest
    ) {
        return Result.response(orderService.updateOrder(user, updateOrderRequest.getId(), updateOrderRequest));
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<Result<Order>> deleteOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        return Result.response(orderService.cancelOrder(user, id));
    }

    @PostMapping("{id}/submit")
    public ResponseEntity<Result<Order>> submitOrder(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        return Result.response(orderService.submitOrder(user, id));
    }

    @GetMapping("get/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(
            @AuthenticationPrincipal User user,
            @PathVariable String status
    ) {
        return ResponseEntity.ok(orderService.viewOrdersByStatus(user, status));
    }

    @GetMapping("all/{status}")
    public ResponseEntity<List<Order>> getAllOrdersFiltered(
            @PathVariable String status
    ) {
        return ResponseEntity.ok(orderService.viewAllOrders(status));
    }

    @GetMapping("{id}")
    public ResponseEntity<Result<Order>> viewOrderDetails(
            @PathVariable Long id
    ) {
        return Result.response(orderService.viewOrderDetails(id));
    }

    @PutMapping("{id}/approve")
    public ResponseEntity<Result<Order>> approveOrder(
            @PathVariable Long id
    ) {
        return Result.response(orderService.approveOrder(id));
    }

    @PutMapping("{id}/decline")
    public ResponseEntity<Result<Order>> declineOrder(
            @PathVariable Long id,
            @RequestBody DeclineReason declineReason
    ) {
        return Result.response(orderService.declineOrder(id, declineReason.reason));
    }


    public record CreateOrderRequest(
            List<OrderItemRequest> orderItems
    ) {
    }

    public record OrderItemRequest(
            Long itemId,
            int requestedQuantity
    ) {
    }

    public record DeclineReason(
            String reason
    ) {
    }
}
