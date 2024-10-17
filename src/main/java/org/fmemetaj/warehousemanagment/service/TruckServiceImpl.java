package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.Truck;
import org.fmemetaj.warehousemanagment.repository.TruckRepository;
import org.springframework.stereotype.Service;

@Service
public class TruckServiceImpl implements TruckService {

    private final TruckRepository truckRepository;

    public TruckServiceImpl(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    @Override
    public Result<Truck> addTruck(Truck truck) {
        return null;
    }

    @Override
    public Result<Truck> updateTruck(Truck truck) {
        return null;
    }

    @Override
    public void deleteTruck(String chassisNumber) {

    }

    @Override
    public Result<Truck> getTruck(String chassisNumber) {
        return null;
    }
}
