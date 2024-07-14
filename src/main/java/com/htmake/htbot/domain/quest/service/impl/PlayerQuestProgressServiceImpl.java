package com.htmake.htbot.domain.quest.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.misc.entity.Misc;
import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.quest.entity.PlayerQuest;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.quest.exception.NotFoundPlayerTargetMonsterException;
import com.htmake.htbot.domain.quest.exception.NotFoundQuestException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.quest.entity.reward.QuestReward;
import com.htmake.htbot.domain.quest.entity.target.misc.TargetMisc;
import com.htmake.htbot.domain.quest.entity.target.monster.PlayerTargetMonster;
import com.htmake.htbot.domain.quest.entity.target.monster.TargetMonster;
import com.htmake.htbot.domain.quest.repository.PlayerQuestRepository;
import com.htmake.htbot.domain.quest.repository.PlayerTargetMonsterRepository;
import com.htmake.htbot.domain.quest.service.PlayerQuestProgressService;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.repository.MainQuestRepository;
import com.htmake.htbot.domain.quest.exception.NotEnoughItemQuantityException;
import com.htmake.htbot.domain.quest.exception.NotEnoughMonsterQuantityException;
import com.htmake.htbot.global.annotation.TransactionalService;
import com.htmake.htbot.global.util.PlayerUtil;
import com.htmake.htbot.global.util.QuestUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;

@TransactionalService
@RequiredArgsConstructor
public class PlayerQuestProgressServiceImpl implements PlayerQuestProgressService {

    private final PlayerQuestRepository playerQuestRepository;
    private final MainQuestRepository mainQuestRepository;
    private final PlayerRepository playerRepository;
    private final InventoryRepository inventoryRepository;
    private final PlayerTargetMonsterRepository playerTargetMonsterRepository;

    private final PlayerUtil playerUtil;
    private final QuestUtil questUtil;

    @Override
    public void execute(String playerId) {
        PlayerQuest playerQuest = playerQuestRepository.findByPlayerId(playerId)
                .orElseThrow(NotFoundQuestException::new);

        Long progress = playerQuest.getMainQuest().getId();

        MainQuest mainQuest = mainQuestRepository.findById(progress)
                .orElseThrow(NotFoundQuestException::new);

        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        validMonsterQuantity(playerId, mainQuest);

        validMiscQuantity(playerId, mainQuest);

        obtainReward(player, mainQuest);

        MainQuest newMainQuest = mainQuestRepository.findById(progress + 1)
                        .orElseThrow(NotFoundQuestException::new);

        playerQuest.setMainQuest(newMainQuest);
        playerQuest.setReadDialogue(false);
        questUtil.initialSet(player, newMainQuest);
        playerQuestRepository.save(playerQuest);

        playerUtil.executeLevelUp(player, mainQuest.getGold(), mainQuest.getExp());
    }

    private void validMonsterQuantity(String playerId, MainQuest mainQuest) {
        List<TargetMonster> targetMonsterList = mainQuest.getTargetMonsterList();

        if (targetMonsterList == null) {
            return;
        }

        for (TargetMonster targetMonster : targetMonsterList) {
            PlayerTargetMonster playerTargetMonster = playerTargetMonsterRepository.findByPlayerIdAndTargetMonster(playerId, targetMonster)
                    .orElseThrow(NotFoundPlayerTargetMonsterException::new);

            if (playerTargetMonster.getCurrentQuantity() < targetMonster.getRequiredQuantity()) {
                throw new NotEnoughMonsterQuantityException();
            }

            playerTargetMonsterRepository.delete(playerTargetMonster);
        }
    }

    private void validMiscQuantity(String playerId, MainQuest mainQuest) {
        List<TargetMisc> targetMiscList = mainQuest.getTargetMiscList();

        if (targetMiscList == null) {
            return;
        }

        for (TargetMisc targetMisc : targetMiscList) {
            Misc misc = targetMisc.getMisc();

            Inventory targetItem = inventoryRepository.findByPlayerIdAndItemId(playerId, misc.getId())
                    .orElse(null);

            int targetItemQuantity = (targetItem == null ? 0 : targetItem.getQuantity());

            if (targetItemQuantity < targetMisc.getRequiredQuantity()) {
                throw new NotEnoughItemQuantityException();
            }

            targetItem.setQuantity(targetItem.getQuantity() - targetMisc.getRequiredQuantity());

            if (targetItem.getQuantity() == 0) {
                inventoryRepository.delete(targetItem);
            } else {
                inventoryRepository.save(targetItem);
            }
        }
    }

    private void obtainReward(Player player, MainQuest mainQuest) {
        List<QuestReward> questRewardList = mainQuest.getQuestRewardList();

        if (questRewardList == null) {
            return;
        }

        for (QuestReward questReward : questRewardList) {
            Inventory existingItem = inventoryRepository.findByPlayerIdAndItemId(player.getId(), questReward.getItemId())
                    .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + questReward.getItemQuantity());
                inventoryRepository.save(existingItem);
            } else {
                Inventory inventory = Inventory.builder()
                        .itemId(questReward.getItemId())
                        .player(player)
                        .name(questReward.getItemName())
                        .quantity(questReward.getItemQuantity())
                        .build();

                inventoryRepository.save(inventory);
            }
        }
    }
}
