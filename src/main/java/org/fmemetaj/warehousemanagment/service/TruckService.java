package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.Truck;

import java.util.List;

public interface TruckService {

    List<Truck> getAllTrucks();

    Result<Truck> getTruck(String chassisNumber);

    Result<Truck> addTruck(Truck truck);

    Result<Truck> updateTruck(Truck truck);

    boolean deleteTruck(String chassisNumber);
}
