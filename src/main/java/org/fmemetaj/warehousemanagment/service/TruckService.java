package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.Truck;

public interface TruckService {

    Result<Truck> addTruck(Truck truck);

    Result<Truck> updateTruck(Truck truck);

    void deleteTruck(String chassisNumber);

    Result<Truck> getTruck(String chassisNumber);
}
