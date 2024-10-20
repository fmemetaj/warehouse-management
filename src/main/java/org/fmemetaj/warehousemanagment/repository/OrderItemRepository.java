package org.fmemetaj.warehousemanagment.repository;

import org.fmemetaj.warehousemanagment.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
