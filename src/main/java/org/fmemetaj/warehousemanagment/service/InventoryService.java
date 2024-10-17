package org.fmemetaj.warehousemanagment.service;

import org.fmemetaj.warehousemanagment.entity.InventoryItem;
import org.fmemetaj.warehousemanagment.entity.Result;

import java.util.List;

public interface InventoryService {

    List<InventoryItem> getInventory();

    Result<InventoryItem> getItem(Long itemId);

    Result<InventoryItem> addItem(InventoryItem item);

    Result<InventoryItem> updateItem(InventoryItem item);

    boolean deleteItem(Long itemId);

}
