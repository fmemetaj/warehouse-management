package org.fmemetaj.warehousemanagment.repository;

import org.fmemetaj.warehousemanagment.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TruckRepository extends JpaRepository<Truck, Long> {

    Optional<Truck> findByChassis(String chassisNumber);
    void deleteByChassis(String chassisNumber);
}
