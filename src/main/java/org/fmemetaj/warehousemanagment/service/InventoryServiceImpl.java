package org.fmemetaj.warehousemanagment.service;

import lombok.extern.slf4j.Slf4j;
import org.fmemetaj.warehousemanagment.entity.InventoryItem;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }


    @Override
    public Result<InventoryItem> addItem(InventoryItem item) {
        return null;
    }

    @Override
    public Result<InventoryItem> updateItem(InventoryItem item) {
        return null;
    }

    @Override
    public void deleteItem(String itemName) {

    }

    @Override
    public Result<InventoryItem> getItem(String itemName) {
        return null;
    }

    @Override
    public void updateItemQuantities(InventoryItem order) {

    }
}
