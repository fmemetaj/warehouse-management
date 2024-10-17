package org.fmemetaj.warehousemanagment.repository;

import org.fmemetaj.warehousemanagment.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
