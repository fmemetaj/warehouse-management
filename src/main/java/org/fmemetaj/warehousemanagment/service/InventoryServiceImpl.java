package org.fmemetaj.warehousemanagment.service;

import lombok.extern.slf4j.Slf4j;
import org.fmemetaj.warehousemanagment.entity.InventoryItem;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<InventoryItem> getInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public Result<InventoryItem> getItem(Long itemId) {
        return inventoryRepository.findById(itemId)
                .map(Result::successful)
                .orElse(Result.error(Result.Code.INVENTORY_ITEM_NOT_FOUND));
    }

    @Override
    @Transactional
    public Result<InventoryItem> addItem(InventoryItem item) {
        var newItem = new InventoryItem()
                .setName(item.getName())
                .setQuantity(item.getQuantity())
                .setUnitPrice(item.getUnitPrice());

        return Result.successful(inventoryRepository.save(newItem));
    }

    @Override
    @Transactional
    public Result<InventoryItem> updateItem(InventoryItem item) {
        var foundItemOpt = inventoryRepository.findById(item.getId());
        if (foundItemOpt.isEmpty()) {
            return Result.error(Result.Code.INVENTORY_ITEM_NOT_FOUND);
        }

        var foundItem = foundItemOpt.get();
        Optional.ofNullable(item.getName()).ifPresent(foundItem::setName);
        Optional.of(item.getQuantity()).ifPresent(foundItem::setQuantity);
        Optional.of(item.getUnitPrice()).ifPresent(foundItem::setUnitPrice);

        return Result.successful(inventoryRepository.save(foundItem));
    }

    @Override
    @Transactional
    public boolean deleteItem(Long itemId) {
        var foundItemOpt = inventoryRepository.findById(itemId);
        foundItemOpt.ifPresent(foundItem -> inventoryRepository.deleteById(itemId));
        return foundItemOpt.isPresent();
    }
}
