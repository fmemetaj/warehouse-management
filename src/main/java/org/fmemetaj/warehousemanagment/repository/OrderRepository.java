package org.fmemetaj.warehousemanagment.repository;

import org.fmemetaj.warehousemanagment.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
