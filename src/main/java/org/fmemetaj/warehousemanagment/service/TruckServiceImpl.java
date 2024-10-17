package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.entity.Truck;
import org.fmemetaj.warehousemanagment.repository.TruckRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TruckServiceImpl implements TruckService {

    private final TruckRepository truckRepository;

    public TruckServiceImpl(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    @Override
    public List<Truck> getAllTrucks() {
        return truckRepository.findAll();
    }

    @Override
    public Result<Truck> getTruck(String chassisNumber) {
        return truckRepository.findByChassis(chassisNumber)
                .map(Result::successful)
                .orElse(Result.error(Result.Code.TRUCK_NOT_FOUND));
    }

    @Override
    @Transactional
    public Result<Truck> addTruck(Truck truck) {
        var newTruck = new Truck()
                .setChassis(truck.getChassis())
                .setLicensePlate(truck.getLicensePlate());

        return Result.successful(truckRepository.save(newTruck));
    }

    @Override
    @Transactional
    public Result<Truck> updateTruck(Truck truck) {
        var foundTruckOpt = truckRepository.findByChassis(truck.getChassis());
        if (foundTruckOpt.isEmpty()) {
            return Result.error(Result.Code.TRUCK_NOT_FOUND);
        }

        var foundTruck = foundTruckOpt.get();
        Optional.ofNullable(truck.getChassis()).ifPresent(foundTruck::setChassis);
        Optional.ofNullable(truck.getLicensePlate()).ifPresent(foundTruck::setLicensePlate);

        return Result.successful(truckRepository.save(foundTruck));
    }

    @Override
    @Transactional
    public boolean deleteTruck(String chassisNumber) {
        var foundTruckOpt = truckRepository.findByChassis(chassisNumber);
        foundTruckOpt.ifPresent(truck -> truckRepository.deleteByChassis(chassisNumber));
        return foundTruckOpt.isPresent();
    }
}
