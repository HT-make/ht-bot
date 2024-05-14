package com.htmake.htbot.domain.dungeon.service.impl;

import com.htmake.htbot.domain.dungeon.entity.Dungeon;
import com.htmake.htbot.domain.dungeon.exception.DungeonNotFoundException;
import com.htmake.htbot.domain.dungeon.presentation.data.response.BossDungeonInfoResponse;
import com.htmake.htbot.domain.dungeon.presentation.data.response.DungeonKeyResponse;
import com.htmake.htbot.domain.dungeon.repository.DungeonRepository;
import com.htmake.htbot.domain.dungeon.service.BossDungeonInfoService;
import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.monster.entity.Monster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BossDungeonInfoServiceImpl implements BossDungeonInfoService {

    private final DungeonRepository dungeonRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public BossDungeonInfoResponse execute(String dungeonId, String playerId) {
        Dungeon dungeon = dungeonRepository.findById(dungeonId)
                .orElseThrow(DungeonNotFoundException::new);

        List<DungeonKeyResponse> dungeonKeyResponseList = dungeon.getDungeonKeyList().stream()
                .map(dungeonKey -> {
                    Inventory item = inventoryRepository.findByPlayerIdAndItemId(playerId, dungeonKey.getItemId())
                            .orElse(null);

                    int playerQuantity = item != null ? item.getQuantity() : 0;

                    return DungeonKeyResponse.builder()
                            .name(dungeonKey.getName())
                            .requireQuantity(dungeonKey.getQuantity())
                            .playerQuantity(playerQuantity)
                            .build();
                })
                .collect(Collectors.toList());

        List<String> monsterNameList = dungeon.getMonsterList().stream()
                .map(Monster::getName)
                .collect(Collectors.toList());

        return BossDungeonInfoResponse.builder()
                .name(dungeon.getName())
                .dungeonKeyList(dungeonKeyResponseList)
                .monsterNameList(monsterNameList)
                .build();
    }
}
