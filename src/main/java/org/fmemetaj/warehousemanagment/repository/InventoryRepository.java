package org.fmemetaj.warehousemanagment.repository;

import org.fmemetaj.warehousemanagment.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
}
