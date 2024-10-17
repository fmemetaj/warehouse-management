package org.fmemetaj.warehousemanagment.controller;

import org.fmemetaj.warehousemanagment.entity.Delivery;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(final DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<Result<Delivery>> scheduleDelivery(@RequestBody ScheduleDeliveryRequest request) {
        return Result.response(deliveryService.scheduleDelivery(
                request.orderId,
                request.deliveryDate,
                request.truckIds
        ));
    }

    @PostMapping("/complete/{deliveryId}")
    public ResponseEntity<?> completeDelivery(
            @PathVariable Long deliveryId
    ) {
        return Result.response(deliveryService.completeDelivery(deliveryId));
    }

    public record ScheduleDeliveryRequest(
            Long orderId,
            Date deliveryDate,
            List<Long> truckIds
    ) {
    }
}
