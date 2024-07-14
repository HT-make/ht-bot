package com.htmake.htbot.domain.quest.service.impl;

import com.htmake.htbot.domain.quest.entity.PlayerQuest;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.quest.exception.NotFoundPlayerTargetMonsterException;
import com.htmake.htbot.domain.quest.exception.NotFoundQuestException;
import com.htmake.htbot.domain.quest.entity.target.monster.PlayerTargetMonster;
import com.htmake.htbot.domain.quest.entity.target.monster.TargetMonster;
import com.htmake.htbot.domain.quest.presentation.data.request.PlayerQuestMonsterQuantityRequest;
import com.htmake.htbot.domain.quest.repository.PlayerQuestRepository;
import com.htmake.htbot.domain.quest.repository.PlayerTargetMonsterRepository;
import com.htmake.htbot.domain.quest.service.PlayerQuestMonsterQuantityService;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.repository.MainQuestRepository;
import com.htmake.htbot.global.annotation.TransactionalService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@TransactionalService
@RequiredArgsConstructor
public class PlayerQuestMonsterQuantityServiceImpl implements PlayerQuestMonsterQuantityService {

    private final PlayerQuestRepository playerQuestRepository;
    private final MainQuestRepository mainQuestRepository;
    private final PlayerTargetMonsterRepository playerTargetMonsterRepository;

    @Override
    public void execute(String playerId, PlayerQuestMonsterQuantityRequest request) {
        PlayerQuest playerQuest = playerQuestRepository.findByPlayerId(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        if (!playerQuest.isReadDialogue()) {
            return;
        }

        Long progress = playerQuest.getMainQuest().getId();

        MainQuest mainQuest = mainQuestRepository.findById(progress)
                .orElseThrow(NotFoundQuestException::new);

        List<TargetMonster> targetMonsterList = mainQuest.getTargetMonsterList();

        for (TargetMonster targetMonster : targetMonsterList) {
            if (targetMonster.getMonster().getName().equals(request.getName())) {
                PlayerTargetMonster playerTargetMonster = playerTargetMonsterRepository.findByPlayerIdAndTargetMonster(playerId, targetMonster)
                        .orElseThrow(NotFoundPlayerTargetMonsterException::new);

                playerTargetMonster.addCurrentQuantity();
                playerTargetMonsterRepository.save(playerTargetMonster);
            }
        }
    }
}
