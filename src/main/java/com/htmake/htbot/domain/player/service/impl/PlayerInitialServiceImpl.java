package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.service.PlayerInitialService;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.entity.Quest;
import com.htmake.htbot.domain.quest.repository.MainQuestRepository;
import com.htmake.htbot.domain.quest.repository.QuestRepository;
import com.htmake.htbot.domain.skill.repository.PlayerSkillRepository;
import com.htmake.htbot.global.annotation.TransactionalService;
import com.htmake.htbot.global.util.SkillUtil;
import lombok.RequiredArgsConstructor;

@TransactionalService
@RequiredArgsConstructor
public class PlayerInitialServiceImpl implements PlayerInitialService {

    private final PlayerRepository playerRepository;

    private final PlayerSkillRepository playerSkillRepository;
    private final SkillUtil skillUtil;

    private final MainQuestRepository mainQuestRepository;
    private final QuestRepository questRepository;

    @Override
    public void execute(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        playerSkillRepository.saveAll(skillUtil.buildPlayerSkillList(player, player.getJob()));

        MainQuest mainQuest = mainQuestRepository.findById(1L)
                .orElse(null);

        Quest quest = Quest.builder()
                .player(player)
                .mainQuest(mainQuest)
                .monsterQuantity(0)
                .build();

        questRepository.save(quest);
    }
}
