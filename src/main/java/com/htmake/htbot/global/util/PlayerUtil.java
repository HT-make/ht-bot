package com.htmake.htbot.global.util;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.presentation.data.response.LevelUpResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerUtil {
    private final PlayerRepository playerRepository;

    public LevelUpResponse executeLevelUp(Player player, int exp, int addedGold) {
        int currentExp = player.getCurrentExp() + exp;
        int gold = player.getGold() + addedGold;

        int maxExp = player.getMaxExp();
        int level = player.getLevel();

        boolean levelUp = false;

        while (currentExp >= maxExp) {
            currentExp -= maxExp;
            maxExp *= level % 5 == 0 ? 2 : 1.3;
            level++;
            player.levelUp(maxExp);
            levelUp = true;
        }

        player.killMonster(currentExp, gold);

        playerRepository.save(player);

        return LevelUpResponse.builder().levelUp(levelUp).build();
    }
}
