package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.entity.*;
import org.fmemetaj.warehousemanagment.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final TruckRepository truckRepository;
    private final InventoryRepository inventoryRepository;
    private final DeliveryTruckRepository deliveryTruckRepository;

    public DeliveryServiceImpl(OrderRepository orderRepository,
                               DeliveryRepository deliveryRepository,
                               TruckRepository truckRepository,
                               InventoryRepository inventoryRepository,
                               DeliveryTruckRepository deliveryTruckRepository) {
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.truckRepository = truckRepository;
        this.inventoryRepository = inventoryRepository;
        this.deliveryTruckRepository = deliveryTruckRepository;
    }

    @Override
    @Transactional
    public Result<Delivery> scheduleDelivery(Long orderId, Date deliveryDate, List<Long> truckIds) {

        var calendar = Calendar.getInstance();
        calendar.setTime(deliveryDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY) {
            return Result.error(Result.Code.DELIVERY_CANNOT_BE_SCHEDULED_ON_SUNDAY);
        }

        var orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Result.error(Result.Code.ORDER_NOT_FOUND);
        }

        var order = orderOpt.get();
        if (Order.OrderStatus.APPROVED != order.getStatus()) {
            return Result.error(Result.Code.ORDER_NOT_APPROVED);
        }

        var trucks = truckRepository.findAllById(truckIds);
        if (trucks.size() != truckIds.size()) {
            return Result.error(Result.Code.TRUCKS_NOT_FOUND);
        }

        for (var truck : trucks) {
            boolean isTruckBooked = deliveryTruckRepository.existsByTruckAndDeliveryDeliveryDate(truck, deliveryDate);
            if (isTruckBooked) {
                return Result.error(Result.Code.TRUCK_NOT_AVAILABLE);
            }
        }

        int totalItems = order.getItems().stream().mapToInt(OrderItem::getRequestedQuantity).sum();
        int maxTruckCapacity = trucks.size() * 10;
        if (totalItems > maxTruckCapacity) {
            return Result.error(Result.Code.EXCEEDS_TRUCK_CAPACITY);
        }

        for (var orderItem : order.getItems()) {
            var inventoryItem = orderItem.getInventoryItem();
            int availableQuantity = inventoryItem.getQuantity();
            int requestedQuantity = orderItem.getRequestedQuantity();
            if (availableQuantity < requestedQuantity) {
                return Result.error(Result.Code.INSUFFICIENT_INVENTORY);
            }
            inventoryItem.setQuantity(availableQuantity - requestedQuantity);
            inventoryRepository.save(inventoryItem);
        }

        var delivery = deliveryRepository.save(new Delivery()
                .setDeliveryDate(deliveryDate)
                .setOrder(order));

        var deliveryTrucks = trucks.stream()
                .map(truck -> new DeliveryTruck()
                        .setDelivery(delivery)
                        .setTruck(truck))
                .collect(Collectors.toList());

        deliveryTruckRepository.saveAll(deliveryTrucks);
        delivery.setDeliveryTrucks(deliveryTrucks);

        order.setStatus(Order.OrderStatus.UNDER_DELIVERY);
        orderRepository.save(order);

        return Result.successful(deliveryRepository.save(delivery));
    }

    @Override
    @Transactional
    public Result<Order> completeDelivery(Long deliveryId) {
        var deliveryOpt = deliveryRepository.findById(deliveryId);
        if (deliveryOpt.isEmpty()) {
            return Result.error(Result.Code.DELIVERY_NOT_FOUND);
        }
        var delivery = deliveryOpt.get();

        var order = delivery.getOrder();
        if (order.getStatus() != Order.OrderStatus.UNDER_DELIVERY) {
            return Result.error(Result.Code.ORDER_CANNOT_BE_COMPLETED);
        }

        order.setStatus(Order.OrderStatus.FULFILLED);
        orderRepository.save(order);

        if (delivery.getStatus() != null) {
            delivery.setStatus(Delivery.DeliveryStatus.COMPLETED);
            deliveryRepository.save(delivery);
        }

        return Result.successful(order);
    }
}
