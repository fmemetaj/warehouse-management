package org.fmemetaj.warehousemanagment.service;

import lombok.extern.slf4j.Slf4j;
import org.fmemetaj.warehousemanagment.entity.InventoryItem;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<InventoryItem> getAllItems(User user) {
        return inventoryRepository.findAll();
    }

    public InventoryItem createItem(InventoryItem item) {
        return inventoryRepository.save(item);
    }

    public void deleteItem(Long itemId) {
        inventoryRepository.deleteById(itemId);
    }
}
