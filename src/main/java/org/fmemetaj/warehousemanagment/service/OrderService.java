package org.fmemetaj.warehousemanagment.service;

import lombok.NonNull;
import org.fmemetaj.warehousemanagment.controller.OrderController;
import org.fmemetaj.warehousemanagment.entity.Order;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.User;

import java.util.List;

public interface OrderService {

    Result<Order> createOrder(User user, List<OrderController.OrderItemRequest> requestOrderItems);

    Result<Order> updateOrder(User user, @NonNull Long orderId, Order updateOrderRequest);

    Result<Order> cancelOrder(User user, Long orderId);

    Result<Order> submitOrder(User user, Long orderId);

    List<Order> viewOrdersByStatus(User user, String status);

    List<Order> viewAllOrders(String status);

    Result<Order> viewOrderDetails(Long orderId);

    Result<Order> approveOrder(Long orderId);

    Result<Order> declineOrder(Long orderId, String reason);

}
