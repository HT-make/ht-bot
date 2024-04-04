package com.htmake.htbot.domain.inventory.service;

import com.htmake.htbot.domain.inventory.presentation.data.request.InventoryInsertItemRequest;

public interface InventoryInsertItemService {
    void execute(String playerId, InventoryInsertItemRequest request);
}
