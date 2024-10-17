package org.fmemetaj.warehousemanagment.controller;

import org.fmemetaj.warehousemanagment.entity.InventoryItem;
import org.fmemetaj.warehousemanagment.entity.Result;
import org.fmemetaj.warehousemanagment.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryItem> getInventory() {
        return inventoryService.getInventory();
    }

    @GetMapping("{id}")
    public ResponseEntity<Result<InventoryItem>> getInventoryItem(
            @PathVariable Long id
    ) {
        return Result.response(inventoryService.getItem(id));
    }

    @PostMapping("add")
    public ResponseEntity<Result<InventoryItem>> addInventoryItem(
            @RequestBody InventoryItem inventoryItem
    ) {
        return Result.response(inventoryService.addItem(inventoryItem));
    }

    @PutMapping("update")
    public ResponseEntity<Result<InventoryItem>> updateInventoryItem(
            @RequestBody InventoryItem inventoryItem
    ) {
        return Result.response(inventoryService.updateItem(inventoryItem));
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<?> deleteInventoryItem(
            @PathVariable Long id
    ) {
        return inventoryService.deleteItem(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inventory Item could not be deleted");
    }
}
