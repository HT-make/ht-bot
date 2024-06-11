package com.htmake.htbot.domain.inventory.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.presentation.data.request.InventoryInsertItemRequest;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.inventory.service.InventoryInsertItemService;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.global.annotation.TransactionalService;
import lombok.RequiredArgsConstructor;

@TransactionalService
@RequiredArgsConstructor
public class InventoryInsertItemServiceImpl implements InventoryInsertItemService {
    private final InventoryRepository inventoryRepository;
    private final PlayerRepository playerRepository;

    @Override
    public void execute(String playerId, InventoryInsertItemRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow();

        Inventory existingItem = inventoryRepository
                .findByPlayerIdAndItemId(playerId, request.getItemId()).orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            inventoryRepository.save(existingItem);
        } else {
            Inventory inventory = Inventory.builder()
                    .itemId(request.getItemId())
                    .player(player)
                    .name(request.getName())
                    .quantity(request.getQuantity())
                    .build();

            inventoryRepository.save(inventory);
        }
    }
}