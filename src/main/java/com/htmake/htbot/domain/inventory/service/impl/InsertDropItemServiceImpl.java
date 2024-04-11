package com.htmake.htbot.domain.inventory.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.inventory.presentation.data.request.InsertDropItemListRequest;
import com.htmake.htbot.domain.inventory.presentation.data.request.DropItemRequest;
import com.htmake.htbot.domain.inventory.service.InsertDropItemService;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InsertDropItemServiceImpl implements InsertDropItemService {

    private final PlayerRepository playerRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public void execute(String playerId, InsertDropItemListRequest request) {

        Player player = playerRepository.findById(playerId).orElseThrow();

        List<DropItemRequest> getItemList = request.getDropItemList();

        for (DropItemRequest dropItemRequest : getItemList) {
            insertItem(player, dropItemRequest);
        }
    }

    private void insertItem(Player player, DropItemRequest request) {
        Inventory existingItem = inventoryRepository
                .findByPlayerIdAndItemId(player.getId(), request.getId()).orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            inventoryRepository.save(existingItem);
        } else {
            Inventory inventory = Inventory.builder()
                    .itemId(request.getId())
                    .player(player)
                    .name(request.getName())
                    .quantity(1)
                    .build();

            inventoryRepository.save(inventory);
        }
    }
}
