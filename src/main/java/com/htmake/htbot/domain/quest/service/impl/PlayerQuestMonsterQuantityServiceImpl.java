package com.htmake.htbot.domain.quest.service.impl;

import com.htmake.htbot.domain.monster.entity.Monster;
import com.htmake.htbot.domain.monster.repository.MonsterRepository;
import com.htmake.htbot.domain.quest.entity.PlayerQuest;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.exception.NotFoundQuestException;
import com.htmake.htbot.domain.quest.presentation.data.request.PlayerQuestMonsterQuantityRequest;
import com.htmake.htbot.domain.quest.repository.PlayerQuestRepository;
import com.htmake.htbot.domain.quest.service.PlayerQuestMonsterQuantityService;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.repository.MainQuestRepository;
import com.htmake.htbot.global.annotation.TransactionalService;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@TransactionalService
@RequiredArgsConstructor
public class PlayerQuestMonsterQuantityServiceImpl implements PlayerQuestMonsterQuantityService {
    private final PlayerQuestRepository playerQuestRepository;
    private final MonsterRepository monsterRepository;
    private final MainQuestRepository mainQuestRepository;

    @Override
    public void execute(String playerId, PlayerQuestMonsterQuantityRequest request) {
        PlayerQuest playerQuest = playerQuestRepository.findByPlayerId(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        MainQuest mainQuest = mainQuestRepository.findById(playerQuest.getMainQuest().getId())
                .orElseThrow(NotFoundQuestException::new);

        Monster targetMonster = monsterRepository.findByName(mainQuest.getTargetMonster().getName()).orElse(null);

        if (targetMonster != null && Objects.equals(targetMonster.getName(), request.getName())) {
            playerQuest.setMonsterQuantity(playerQuest.getMonsterQuantity() + 1);
            playerQuestRepository.save(playerQuest);
        }
    }
}
