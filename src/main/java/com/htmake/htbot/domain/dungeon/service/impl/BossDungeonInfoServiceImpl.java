package com.htmake.htbot.domain.dungeon.service.impl;

import com.htmake.htbot.domain.dungeon.entity.Dungeon;
import com.htmake.htbot.domain.dungeon.exception.DungeonNotFoundException;
import com.htmake.htbot.domain.dungeon.presentation.data.response.BossDungeonInfoResponse;
import com.htmake.htbot.domain.dungeon.repository.DungeonRepository;
import com.htmake.htbot.domain.dungeon.service.BossDungeonInfoService;
import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.misc.entity.Misc;
import com.htmake.htbot.domain.misc.exception.MiscNotFoundException;
import com.htmake.htbot.domain.misc.repository.MiscRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BossDungeonInfoServiceImpl implements BossDungeonInfoService {

    private final DungeonRepository dungeonRepository;
    private final InventoryRepository inventoryRepository;
    private final MiscRepository miscRepository;

    @Override
    public BossDungeonInfoResponse execute(String dungeonId, String playerId) {
        Dungeon dungeon = dungeonRepository.findById(dungeonId)
                .orElseThrow(DungeonNotFoundException::new);

        Misc misc = miscRepository.findById(dungeon.getKey())
                .orElseThrow(MiscNotFoundException::new);

        Inventory item = inventoryRepository.findByPlayerIdAndItemId(playerId, dungeon.getKey())
                .orElse(null);

        int quantity = item != null ? item.getQuantity() : 0;

        return BossDungeonInfoResponse.builder()
                .name(dungeon.getName())
                .dungeonKey(misc.getName())
                .playerKeyQuantity(quantity)
                .build();
    }
}
