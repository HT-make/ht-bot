package com.htmake.htbot.domain.inventory.presentation;

import com.htmake.htbot.domain.inventory.presentation.data.request.InventoryInsertItemRequest;
import com.htmake.htbot.domain.inventory.presentation.data.response.InventoryInfoListResponse;
import com.htmake.htbot.domain.inventory.service.InventoryInfoService;
import com.htmake.htbot.domain.inventory.service.InventoryInsertItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryInfoService inventoryInfoService;
    private final InventoryInsertItemService inventoryInsertItemService;

    @GetMapping("/info/{player_id}")
    public ResponseEntity<InventoryInfoListResponse> getInventoryInfo(@PathVariable("player_id") String playerId) {
        InventoryInfoListResponse res = inventoryInfoService.execute(playerId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/insert/{player_id}")
    public ResponseEntity<Void> insertInventory(@PathVariable("player_id") String playerId,
                                                @RequestBody InventoryInsertItemRequest request) {
        inventoryInsertItemService.execute(playerId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
