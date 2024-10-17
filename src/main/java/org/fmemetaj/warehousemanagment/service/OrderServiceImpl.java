package org.fmemetaj.warehousemanagment.service;

import lombok.NonNull;
import org.fmemetaj.warehousemanagment.controller.OrderController;
import org.fmemetaj.warehousemanagment.entity.*;
import org.fmemetaj.warehousemanagment.repository.InventoryRepository;
import org.fmemetaj.warehousemanagment.repository.OrderItemRepository;
import org.fmemetaj.warehousemanagment.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryRepository inventoryRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, InventoryRepository inventoryRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    @Override
    public Result<Order> createOrder(User user, List<OrderController.OrderItemRequest> requestOrderItems) {

        if (requestOrderItems == null || requestOrderItems.isEmpty()) {
            return Result.error(Result.Code.ORDER_ITEM_LIST_EMPTY);
        }

        List<OrderItem> orderItems = requestOrderItems.stream()
                .map(orderItemRequest -> inventoryRepository.findById(orderItemRequest.itemId())
                        .map(item -> new OrderItem()
                                .setInventoryItem(item)
                                .setRequestedQuantity(orderItemRequest.requestedQuantity()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        if (orderItems.isEmpty()) {
            return Result.error(Result.Code.ORDER_ITEM_LIST_EMPTY);
        }

        var order = new Order(user, orderItems);
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        orderRepository.save(order);

        return Result.successful(order);
    }

    @Override
    public Result<Order> updateOrder(User user, @NonNull Long orderId, Order updateOrderRequest) {
        var orderResult = getUserOrder(user, orderId);
        if (orderResult.isFailure()) {
            return Result.error(orderResult.getErrorCode());
        }

        var existingOrder = orderResult.getData();

        if (!(Order.OrderStatus.CREATED == existingOrder.getStatus() || Order.OrderStatus.DECLINED == existingOrder.getStatus())) {
            return Result.error(Result.Code.ORDER_CANNOT_BE_UPDATED);
        }

        if (updateOrderRequest.getItems() == null || updateOrderRequest.getItems().isEmpty()) {
            return Result.error(Result.Code.ORDER_ITEM_LIST_EMPTY);
        }

        // Update the order items
        var updateResult = updateOrderItems(existingOrder, updateOrderRequest.getItems());

        if (!updateResult.isSuccessful()) {
            return Result.error(updateResult.getErrorCode());
        }

        // Save and return the updated order
        var updatedOrder = orderRepository.save(existingOrder);
        return Result.successful(updatedOrder);
    }

    @Override
    public Result<Order> cancelOrder(User user, Long orderId) {
        var orderResult = getUserOrder(user, orderId);
        if (orderResult.isFailure()) {
            return Result.error(orderResult.getErrorCode());
        }

        var existingOrder = orderResult.getData();

        if (Order.OrderStatus.FULFILLED != existingOrder.getStatus()
                && Order.OrderStatus.UNDER_DELIVERY != existingOrder.getStatus()
                && Order.OrderStatus.CANCELED != existingOrder.getStatus()) {

            existingOrder.setStatus(Order.OrderStatus.CANCELED);
            orderRepository.save(existingOrder);
            return Result.successful(existingOrder);
        }
        return Result.error(Result.Code.ORDER_CANNOT_BE_UPDATED);
    }

    @Override
    public Result<Order> submitOrder(User user, Long orderId) {
        var orderResult = getUserOrder(user, orderId);
        if (orderResult.isFailure()) {
            return Result.error(orderResult.getErrorCode());
        }

        var existingOrder = orderResult.getData();

        if (Order.OrderStatus.CREATED == existingOrder.getStatus()
                || Order.OrderStatus.DECLINED == existingOrder.getStatus()) {

            existingOrder.setStatus(Order.OrderStatus.AWAITING_APPROVAL);
            return Result.successful(orderRepository.save(existingOrder));
        }
        return Result.error(Result.Code.ORDER_CANNOT_BE_UPDATED);
    }

    @Override
    public List<Order> viewOrdersByStatus(User user, String status) {

        var orderStatusList = Arrays.stream(Order.OrderStatus.values()).toList()
                .stream()
                .map(Enum::name)
                .toList();

        if (!orderStatusList.contains(status)) {
            return Collections.emptyList();
        }

        Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status);

        return orderRepository.findAllByUserIdAndStatus(user.getId(), orderStatus);
    }

    @Override
    public List<Order> viewAllOrders() {
        return null;
    }

    @Override
    public Result<Order> viewOrderDetails(int orderNumber) {
        return null;
    }

    @Override
    public Result<Order> approveOrder(int orderNumber) {
        return null;
    }

    @Override
    public Result<Order> declineOrder(int orderNumber, String reason) {
        return null;
    }

    @Override
    public Result<Order> scheduleDelivery(int orderNumber, Date deliveryDate, List<Truck> trucks) {
        return null;
    }

    @Override
    public Result<Order> markOrderAsFulfilled(int orderNumber) {
        return null;
    }

    private Result<Result.Code> updateOrderItems(Order existingOrder, List<OrderItem> updatedItems) {
        // Remove items not present in the updated items
        removeAbsentItems(existingOrder, updatedItems);

        // Update existing items and add new ones
        return processUpdatedItems(existingOrder, updatedItems);
    }

    private void removeAbsentItems(Order existingOrder, List<OrderItem> updatedItems) {
        // Collect IDs of updated items
        Set<Long> updatedItemIds = updatedItems.stream()
                .map(OrderItem::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Remove items from existing order that are not in updated items
        existingOrder.getItems().removeIf(existingItem -> {
            Long itemId = existingItem.getId();
            if (itemId != null && !updatedItemIds.contains(itemId)) {
                orderItemRepository.delete(existingItem);
                return true; // Remove item from the list
            }
            return false; // Keep item in the list
        });
    }

    private Result<Result.Code> processUpdatedItems(Order existingOrder, List<OrderItem> updatedItems) {
        for (OrderItem updatedItem : updatedItems) {
            if (updatedItem.getId() != null) {
                // Update existing item
                var existingItemOpt = findOrderItemById(existingOrder, updatedItem.getId());

                if (existingItemOpt.isEmpty()) {
                    return Result.error(Result.Code.ORDER_ITEM_NOT_FOUND);
                }

                var existingItem = existingItemOpt.get();
                Optional.of(updatedItem.getRequestedQuantity()).ifPresent(existingItem::setRequestedQuantity);
                orderItemRepository.save(existingItem);
            } else {
                // Add new item
                var newOrderItem = new OrderItem()
                        .setInventoryItem(updatedItem.getInventoryItem())
                        .setOrder(existingOrder)
                        .setRequestedQuantity(updatedItem.getRequestedQuantity());
                existingOrder.getItems().add(newOrderItem);
                orderItemRepository.save(newOrderItem);
            }
        }
        return Result.successful(Result.Code.SUCCESS);
    }

    private Optional<OrderItem> findOrderItemById(Order order, Long itemId) {
        for (OrderItem item : order.getItems()) {
            if (itemId.equals(item.getId())) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    private Result<Order> getUserOrder(User user, Long orderId) {
        // Fetch the order by ID
        var existingOrderOpt = orderRepository.findById(orderId);
        if (existingOrderOpt.isEmpty()) {
            return Result.error(Result.Code.ORDER_NOT_FOUND);
        }

        // Verify that the order belongs to the user
        var existingUserOrderOpt = orderRepository.findByIdAndUserId(orderId, user.getId());
        if (existingUserOrderOpt.isEmpty()) {
            return Result.error(Result.Code.USER_ORDER_NOT_FOUND);
        }

        // Return the existing order
        var existingOrder = existingOrderOpt.get();
        return Result.successful(existingOrder);
    }

    /*@Transactional
    public Result<Order> updateOrder(Order updateOrderRequest) {
        var orderOpt = orderRepository.findById(updateOrderRequest.getId());
        if (orderOpt.isEmpty()) {
            return Result.error(Result.Code.ORDER_NOT_FOUND);
        }

        var order = orderOpt.get();
        if (!(Order.OrderStatus.CREATED.equals(order.getStatus())
                || Order.OrderStatus.DECLINED.equals(order.getStatus()))) {
            return Result.error(Result.Code.ORDER_CANNOT_BE_UPDATED);
        }

        updateOrderRequest.getItems().forEach(
                orderItem -> orderItemRepository.findById(orderItem.getId())
                        .map(updateOrderItemRequest -> {
                            Optional.of(updateOrderItemRequest.getRequestedQuantity()).ifPresent(orderItem::setRequestedQuantity);
                            return orderItemRepository.save(orderItem);
                        })
                        .orElseGet(() -> {
                            var newOrderItem = new OrderItem()
                                    .setInventoryItem(orderItem.getInventoryItem())
                                    .setOrder(order)
                                    .setRequestedQuantity(orderItem.getRequestedQuantity());

                            return orderItemRepository.save(newOrderItem);
                        }));

        if (updateOrderRequest.getItems().isEmpty()) {
            return Result.error(Result.Code.ORDER_ITEM_LIST_EMPTY);
        }

        return Result.successful(orderRepository.save(order));
    }*/
}
