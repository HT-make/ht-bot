package com.htmake.htbot.domain.quest.service.impl;

import com.htmake.htbot.domain.monster.entity.Monster;
import com.htmake.htbot.domain.monster.repository.MonsterRepository;
import com.htmake.htbot.domain.quest.entity.Quest;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.exception.NotFoundQuestException;
import com.htmake.htbot.domain.quest.presentation.data.request.PlayerQuestMonsterQuantityRequest;
import com.htmake.htbot.domain.quest.repository.QuestRepository;
import com.htmake.htbot.domain.quest.service.PlayerQuestMonsterQuantityService;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.repository.MainQuestRepository;
import com.htmake.htbot.global.annotation.TransactionalService;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@TransactionalService
@RequiredArgsConstructor
public class PlayerQuestMonsterQuantityServiceImpl implements PlayerQuestMonsterQuantityService {
    private final QuestRepository questRepository;
    private final MonsterRepository monsterRepository;
    private final MainQuestRepository mainQuestRepository;

    @Override
    public void execute(String playerId, PlayerQuestMonsterQuantityRequest request) {
        Quest quest = questRepository.findByPlayerId(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        MainQuest mainQuest = mainQuestRepository.findById(quest.getMainQuest().getId())
                .orElseThrow(NotFoundQuestException::new);

        Monster targetMonster = monsterRepository.findByName(mainQuest.getTargetMonster().getName()).orElse(null);

        if (targetMonster != null && Objects.equals(targetMonster.getName(), request.getName())) {
            quest.setMonsterQuantity(quest.getMonsterQuantity() + 1);
            questRepository.save(quest);
        }
    }
}
