package org.fmemetaj.warehousemanagment.controller;

import org.fmemetaj.warehousemanagment.entity.InventoryItem;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.service.InventoryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public List<InventoryItem> getAllItems(
            @AuthenticationPrincipal User user
    ) {
        return inventoryService.getAllItems(user);
    }

    @PostMapping("add-item")
    public InventoryItem createItem(@RequestBody InventoryItem item) {
        return inventoryService.createItem(item);
    }

    @DeleteMapping("delete-item/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        inventoryService.deleteItem(itemId);
    }
}
