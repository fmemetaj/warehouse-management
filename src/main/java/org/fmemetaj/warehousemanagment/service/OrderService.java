package org.fmemetaj.warehousemanagment.service;

import lombok.NonNull;
import org.fmemetaj.warehousemanagment.controller.OrderController;
import org.fmemetaj.warehousemanagment.entity.Order;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.Truck;
import org.fmemetaj.warehousemanagment.entity.User;

import java.util.Date;
import java.util.List;

public interface OrderService {

    Result<Order> createOrder(User user, List<OrderController.OrderItemRequest> requestOrderItems);

    Result<Order> updateOrder(User user, @NonNull Long orderId, Order updateOrderRequest);

    Result<Order> cancelOrder(User user, Long orderId);

    Result<Order> submitOrder(User user, Long orderId);

    List<Order> viewOrdersByStatus(User user, String status);

    List<Order> viewAllOrders();

    Result<Order> viewOrderDetails(int orderNumber);

    Result<Order> approveOrder(int orderNumber);

    Result<Order> declineOrder(int orderNumber, String reason);

    Result<Order> scheduleDelivery(int orderNumber, Date deliveryDate, List<Truck> trucks);

    Result<Order> markOrderAsFulfilled(int orderNumber);
}
