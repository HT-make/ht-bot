package com.htmake.htbot.domain.quest.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.quest.entity.Quest;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.exception.NotFoundQuestException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.quest.repository.QuestRepository;
import com.htmake.htbot.domain.quest.service.PlayerQuestProgressService;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.repository.MainQuestRepository;
import com.htmake.htbot.domain.quest.exception.NotEnoughItemQuantityException;
import com.htmake.htbot.domain.quest.exception.NotEnoughMonsterQuantityException;
import com.htmake.htbot.global.annotation.TransactionalService;
import com.htmake.htbot.global.util.PlayerUtil;
import lombok.RequiredArgsConstructor;

@TransactionalService
@RequiredArgsConstructor
public class PlayerQuestProgressServiceImpl implements PlayerQuestProgressService {

    private final QuestRepository questRepository;
    private final MainQuestRepository mainQuestRepository;
    private final PlayerRepository playerRepository;
    private final InventoryRepository inventoryRepository;
    private final PlayerUtil playerUtil;

    @Override
    public void execute(String playerId) {
        Quest quest = questRepository.findByPlayerId(playerId)
                .orElseThrow(NotFoundQuestException::new);

        Long progress = quest.getMainQuest().getId();

        MainQuest mainQuest = mainQuestRepository.findById(progress)
                .orElseThrow(NotFoundQuestException::new);

        Player player = playerRepository.findById(playerId).orElseThrow(NotFoundPlayerException::new);

        Inventory targetItem = inventoryRepository
                .findByPlayerIdAndItemId(playerId, mainQuest.getTargetItem().getId()).orElse(null);

        if (mainQuest.getTargetMonsterQuantity() > quest.getMonsterQuantity()){
            throw new NotEnoughMonsterQuantityException();
        }
        if (targetItem != null && targetItem.getQuantity() >= mainQuest.getTargetItemQuantity()) {
            targetItem.setQuantity(targetItem.getQuantity() - mainQuest.getRewardItemQuantity());
            if (targetItem.getQuantity() == 0) {
                inventoryRepository.delete(targetItem);
            } else {
                inventoryRepository.save(targetItem);
            }
        } else {
            throw new NotEnoughItemQuantityException();
        }

        Inventory existingItem = inventoryRepository
                .findByPlayerIdAndItemId(playerId, mainQuest.getRewardItemId()).orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + mainQuest.getRewardItemQuantity());
            inventoryRepository.save(existingItem);
        } else {
            Inventory inventory = Inventory.builder()
                    .itemId(mainQuest.getRewardItemId())
                    .player(player)
                    .name(mainQuest.getRewardItemName())
                    .quantity(mainQuest.getRewardItemQuantity())
                    .build();

            inventoryRepository.save(inventory);
        }

        quest.setMainQuest(mainQuestRepository.findById(progress + 1).orElse(null));
        quest.setMonsterQuantity(0);
        questRepository.save(quest);

        playerUtil.executeLevelUp(player, mainQuest.getGold(), mainQuest.getExp());
    }
}
