package org.fmemetaj.warehousemanagment.controller;

import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.Truck;
import org.fmemetaj.warehousemanagment.service.TruckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("trucks")
public class TruckController {

    private final TruckService truckService;

    public TruckController(final TruckService truckService) {
        this.truckService = truckService;
    }

    @GetMapping
    public List<Truck> getAllTrucks() {
        return truckService.getAllTrucks();
    }

    @GetMapping("{chassis}")
    public ResponseEntity<Result<Truck>> getTruckByChassis(
            @PathVariable String chassis
    ) {
        return Result.response(truckService.getTruck(chassis));
    }

    @PostMapping("add")
    public ResponseEntity<Result<Truck>> addTruck(
            @RequestBody Truck truck
    ) {
        return Result.response(truckService.addTruck(truck));
    }

    @PutMapping("update")
    public ResponseEntity<Result<Truck>> updateTruck(
            @RequestBody Truck truck
    ) {
        return Result.response(truckService.updateTruck(truck));
    }

    @DeleteMapping("{chassis}/delete")
    public ResponseEntity<?> deleteTruck(
            @PathVariable String chassis
    ) {
        return truckService.deleteTruck(chassis)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Truck could not be deleted");
    }
}
