package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.entity.InventoryItem;
import org.fmemetaj.warehousemanagment.entity.Result;

public interface InventoryService {

    Result<InventoryItem> addItem(InventoryItem item);

    Result<InventoryItem> updateItem(InventoryItem item);

    void deleteItem(String itemName);

    Result<InventoryItem> getItem(String itemName);

    void updateItemQuantities(InventoryItem order);
}
