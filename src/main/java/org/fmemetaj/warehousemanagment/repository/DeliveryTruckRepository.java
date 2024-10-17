package org.fmemetaj.warehousemanagment.repository;

import org.fmemetaj.warehousemanagment.entity.DeliveryTruck;
import org.fmemetaj.warehousemanagment.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface DeliveryTruckRepository extends JpaRepository<DeliveryTruck, Long> {

    boolean existsByTruckAndDeliveryDeliveryDate(Truck truck, Date deliveryDate);

}
