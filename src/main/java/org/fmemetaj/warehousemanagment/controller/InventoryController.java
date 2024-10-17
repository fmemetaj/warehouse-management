package org.fmemetaj.warehousemanagment.controller;

import org.fmemetaj.warehousemanagment.entity.InventoryItem;
import org.fmemetaj.warehousemanagment.entity.User;
import org.fmemetaj.warehousemanagment.service.InventoryServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryServiceImpl inventoryServiceImpl;

    public InventoryController(InventoryServiceImpl inventoryServiceImpl) {
        this.inventoryServiceImpl = inventoryServiceImpl;
    }

}
