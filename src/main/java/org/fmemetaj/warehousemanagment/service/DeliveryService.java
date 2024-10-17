package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.entity.Delivery;
import org.fmemetaj.warehousemanagment.entity.Order;
import org.fmemetaj.warehousemanagment.entity.Result;

import java.util.Date;
import java.util.List;

public interface DeliveryService {

    Result<Delivery> scheduleDelivery(Long orderId, Date deliveryDate, List<Long> truckIds);

    Result<Order> completeDelivery(Long deliveryId);
}
