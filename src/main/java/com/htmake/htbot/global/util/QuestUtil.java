package com.htmake.htbot.global.util;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.entity.target.monster.PlayerTargetMonster;
import com.htmake.htbot.domain.quest.entity.target.monster.TargetMonster;
import com.htmake.htbot.domain.quest.repository.PlayerTargetMonsterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestUtil {

    private final PlayerTargetMonsterRepository playerTargetMonsterRepository;

    public void initialSet(Player player, MainQuest mainQuest) {
        List<TargetMonster> targetMonsterList = mainQuest.getTargetMonsterList();

        if (targetMonsterList != null) {
            for (TargetMonster targetMonster : targetMonsterList) {
                PlayerTargetMonster playerTargetMonster = PlayerTargetMonster.builder()
                        .player(player)
                        .targetMonster(targetMonster)
                        .currentQuantity(0)
                        .build();

                playerTargetMonsterRepository.save(playerTargetMonster);
            }
        }
    }
}
