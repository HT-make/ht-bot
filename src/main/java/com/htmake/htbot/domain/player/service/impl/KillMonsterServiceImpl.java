package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.presentation.data.request.GetDropItemRequest;
import com.htmake.htbot.domain.player.presentation.data.request.KillMonsterRequest;
import com.htmake.htbot.domain.player.presentation.data.response.LevelUpResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.service.KillMonsterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class KillMonsterServiceImpl implements KillMonsterService {

    private final PlayerRepository playerRepository;

    private final InventoryRepository inventoryRepository;

    @Override
    public LevelUpResponse execute(String playerId, KillMonsterRequest request) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow();

        int currentExp = player.getCurrentExp() + request.getExp();
        int gold = player.getGold() + request.getGold();

        int maxExp = player.getMaxExp();
        int level = player.getLevel();

        boolean levelUp = false;

        if (currentExp >= maxExp) {
            currentExp -= maxExp;
            maxExp *= level % 5 == 0 ? 2 : 1.3;
            player.levelUp(maxExp);
            levelUp = true;
        }

        player.killMonster(currentExp, gold);

        playerRepository.save(player);

        List<GetDropItemRequest> getItemList = request.getGetItemList();

        if (!getItemList.isEmpty()) {
            for (GetDropItemRequest dropItemRequest : getItemList) {
                insertItem(player, dropItemRequest);
            }
        }

        return LevelUpResponse.builder().levelUp(levelUp).build();
    }

    private void insertItem(Player player, GetDropItemRequest request) {
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
