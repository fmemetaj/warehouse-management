package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.controller.OrderController;
import org.fmemetaj.warehousemanagment.entity.Order;
import org.fmemetaj.warehousemanagment.entity.OrderItem;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.repository.InventoryRepository;
import org.fmemetaj.warehousemanagment.repository.OrderItemRepository;
import org.fmemetaj.warehousemanagment.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Result<Order> createOrder(User user, List<OrderController.OrderItemRequest> requestOrderItems) {
        var order = new Order().setUser(user);

        List<OrderItem> orderItemList = new ArrayList<>();
        requestOrderItems.forEach(
                orderItem -> inventoryRepository.findById(orderItem.itemId())
                        .map(item -> {
                            var newOrderItem = new OrderItem()
                                    .setInventoryItem(item)
                                    .setOrder(order)
                                    .setRequestedQuantity(orderItem.requestedQuantity());

                            var savedOrderItem = orderItemRepository.save(newOrderItem);
                            orderItemList.add(savedOrderItem);
                            return savedOrderItem;
                        }));

        if (orderItemList.isEmpty()) {
            return Result.error(Result.Code.ORDER_ITEM_LIST_EMPTY);
        }

        order.setItems(orderItemList);

        return Result.successful(orderRepository.save(order));
    }

    @Transactional
    public Result<Order> updateOrder(User user, List<OrderController.OrderItemRequest> requestOrderItems) {
        var order = new Order().setUser(user);

        List<OrderItem> orderItemList = new ArrayList<>();
        requestOrderItems.forEach(
                orderItem -> inventoryRepository.findById(orderItem.itemId())
                        .map(item -> {
                            var newOrderItem = new OrderItem()
                                    .setInventoryItem(item)
                                    .setOrder(order)
                                    .setRequestedQuantity(orderItem.requestedQuantity());

                            var savedOrderItem = orderItemRepository.save(newOrderItem);
                            orderItemList.add(savedOrderItem);
                            return savedOrderItem;
                        }));

        if (orderItemList.isEmpty()) {
            return Result.error(Result.Code.ORDER_ITEM_LIST_EMPTY);
        }

        order.setItems(orderItemList);

        return Result.successful(orderRepository.save(order));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
