package com.htmake.htbot.domain.quest.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.quest.entity.Quest;
import com.htmake.htbot.domain.player.exception.NotFoundQuestException;
import com.htmake.htbot.domain.quest.presentation.data.response.PlayerQuestInfoResponse;
import com.htmake.htbot.domain.quest.repository.QuestRepository;
import com.htmake.htbot.domain.quest.service.PlayerQuestInfoService;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.repository.MainQuestRepository;
import com.htmake.htbot.global.annotation.ReadOnlyService;
import lombok.RequiredArgsConstructor;

@ReadOnlyService
@RequiredArgsConstructor
public class PlayerQuestInfoServiceImpl implements PlayerQuestInfoService {
    private final QuestRepository questRepository;

    private final MainQuestRepository mainQuestRepository;

    private final InventoryRepository inventoryRepository;

    @Override
    public PlayerQuestInfoResponse execute(String playerId) {
        Quest quest = questRepository.findByPlayerId(playerId)
                .orElseThrow(NotFoundQuestException::new);

        Long progress = quest.getMainQuest().getId();

        MainQuest mainQuest = mainQuestRepository.findById(progress)
                .orElseThrow(NotFoundQuestException::new);

        int targetItemQuantity = inventoryRepository.findByPlayerIdAndItemId(playerId, mainQuest.getTargetItem().getId())
                .map(Inventory::getQuantity)
                .orElse(0);

        return PlayerQuestInfoResponse.builder()
                .title(mainQuest.getTitle())
                .description(mainQuest.getDescription())
                .targetMonster(mainQuest.getTargetMonster().getName())
                .targetItem(mainQuest.getTargetItem().getName())
                .targetMonsterQuantity(mainQuest.getTargetMonsterQuantity())
                .targetItemQuantity(mainQuest.getTargetItemQuantity())
                .gold(mainQuest.getGold())
                .exp(mainQuest.getExp())
                .rewardItemName(mainQuest.getRewardItemName())
                .rewardItemQuantity(mainQuest.getRewardItemQuantity())
                .itemQuantity(targetItemQuantity)
                .monsterQuantity(quest.getMonsterQuantity())
                .build();
    }
}
