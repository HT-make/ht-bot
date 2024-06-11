package com.htmake.htbot.domain.dungeon.service.impl;

import com.htmake.htbot.domain.dungeon.entity.Dungeon;
import com.htmake.htbot.domain.dungeon.entity.DungeonKey;
import com.htmake.htbot.domain.dungeon.exception.DungeonNotFoundException;
import com.htmake.htbot.domain.dungeon.presentation.data.response.DungeonResponse;
import com.htmake.htbot.domain.dungeon.presentation.data.response.MonsterResponse;
import com.htmake.htbot.domain.dungeon.repository.DungeonRepository;
import com.htmake.htbot.domain.dungeon.service.BossDungeonEntryService;
import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.exception.InventoryItemNotFoundException;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.monster.entity.Monster;
import com.htmake.htbot.global.annotation.TransactionalService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@TransactionalService
@RequiredArgsConstructor
public class BossDungeonEntryServiceImpl implements BossDungeonEntryService {

    private final DungeonRepository dungeonRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public DungeonResponse execute(String dungeonId, String playerId) {
        Dungeon dungeon = dungeonRepository.findById(dungeonId)
                .orElseThrow(DungeonNotFoundException::new);

        List<DungeonKey> dungeonKeyList = dungeon.getDungeonKeyList();

        for (DungeonKey dungeonKey : dungeonKeyList) {
            Inventory item = inventoryRepository.findByPlayerIdAndItemId(playerId, dungeonKey.getItemId())
                    .orElseThrow(InventoryItemNotFoundException::new);

            if (item.getQuantity() == dungeonKey.getQuantity()) {
                inventoryRepository.delete(item);
            } else {
                item.setQuantity(item.getQuantity() - dungeonKey.getQuantity());
                inventoryRepository.save(item);
            }
        }

        List<Monster> monsterList = dungeon.getMonsterList();

        return DungeonResponse.builder()
                .name(dungeon.getName())
                .monsterList(
                        monsterList.stream()
                                .map(MonsterResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
