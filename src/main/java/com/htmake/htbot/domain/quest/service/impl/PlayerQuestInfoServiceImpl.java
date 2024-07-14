package com.htmake.htbot.domain.quest.service.impl;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.inventory.repository.InventoryRepository;
import com.htmake.htbot.domain.misc.entity.Misc;
import com.htmake.htbot.domain.quest.entity.PlayerQuest;
import com.htmake.htbot.domain.quest.exception.NotFoundPlayerTargetMonsterException;
import com.htmake.htbot.domain.quest.exception.NotFoundQuestException;
import com.htmake.htbot.domain.quest.entity.reward.QuestReward;
import com.htmake.htbot.domain.quest.entity.target.misc.TargetMisc;
import com.htmake.htbot.domain.quest.entity.target.monster.PlayerTargetMonster;
import com.htmake.htbot.domain.quest.entity.target.monster.TargetMonster;
import com.htmake.htbot.domain.quest.presentation.data.response.PlayerQuestInfoResponse;
import com.htmake.htbot.domain.quest.presentation.data.response.RewardResponse;
import com.htmake.htbot.domain.quest.presentation.data.response.TargetResponse;
import com.htmake.htbot.domain.quest.repository.PlayerQuestRepository;
import com.htmake.htbot.domain.quest.repository.PlayerTargetMonsterRepository;
import com.htmake.htbot.domain.quest.service.PlayerQuestInfoService;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.repository.MainQuestRepository;
import com.htmake.htbot.global.annotation.TransactionalService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@TransactionalService
@RequiredArgsConstructor
public class PlayerQuestInfoServiceImpl implements PlayerQuestInfoService {

    private final PlayerQuestRepository playerQuestRepository;
    private final MainQuestRepository mainQuestRepository;
    private final PlayerTargetMonsterRepository playerTargetMonsterRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public PlayerQuestInfoResponse execute(String playerId) {
        PlayerQuest playerQuest = playerQuestRepository.findByPlayerId(playerId)
                .orElseThrow(NotFoundQuestException::new);

        Long progress = playerQuest.getMainQuest().getId();

        MainQuest mainQuest = mainQuestRepository.findById(progress)
                .orElseThrow(NotFoundQuestException::new);

        List<TargetResponse> targetResponseList = new ArrayList<>();
        targetMonster(playerId, mainQuest, targetResponseList);
        targetMisc(playerId, mainQuest, targetResponseList);
        List<QuestReward> questRewardList = mainQuest.getQuestRewardList();

        if (!playerQuest.isReadDialogue()) {
            playerQuest.setReadDialogue(true);
            playerQuestRepository.save(playerQuest);
        }

        return PlayerQuestInfoResponse.builder()
                .title(mainQuest.getTitle())
                .description(mainQuest.getDescription())
                .gold(mainQuest.getGold())
                .exp(mainQuest.getExp())
                .targetList(targetResponseList)
                .rewardList(
                        questRewardList.stream()
                                .map(RewardResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private void targetMonster(String playerId, MainQuest mainQuest, List<TargetResponse> targetResponseList) {
        List<TargetMonster> targetMonsterList = mainQuest.getTargetMonsterList();

        if (targetMonsterList == null) {
            return;
        }

        for (TargetMonster targetMonster : targetMonsterList) {
            PlayerTargetMonster playerTargetMonster = playerTargetMonsterRepository.findByPlayerIdAndTargetMonster(playerId, targetMonster)
                    .orElseThrow(NotFoundPlayerTargetMonsterException::new);

            TargetResponse targetResponse = TargetResponse.builder()
                    .name(targetMonster.getMonster().getName())
                    .requiredQuantity(targetMonster.getRequiredQuantity())
                    .currentQuantity(playerTargetMonster.getCurrentQuantity())
                    .build();

            targetResponseList.add(targetResponse);
        }
    }

    private void targetMisc(String playerId, MainQuest mainQuest, List<TargetResponse> targetResponseList) {
        List<TargetMisc> targetMiscList = mainQuest.getTargetMiscList();

        if (targetMiscList == null) {
            return;
        }

        for (TargetMisc targetMisc : targetMiscList) {
            Misc misc = targetMisc.getMisc();

            int targetItemQuantity = inventoryRepository.findByPlayerIdAndItemId(playerId, misc.getId())
                    .map(Inventory::getQuantity)
                    .orElse(0);

            TargetResponse targetResponse = TargetResponse.builder()
                    .name(misc.getName())
                    .requiredQuantity(targetMisc.getRequiredQuantity())
                    .currentQuantity(targetItemQuantity)
                    .build();

            targetResponseList.add(targetResponse);
        }
    }
}
