package com.htmake.htbot.global.util;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.entity.Status;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.presentation.data.response.LevelUpResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerUtil {

    private final PlayerRepository playerRepository;
    private final StatusRepository statusRepository;

    public LevelUpResponse executeLevelUp(Player player, int exp, int addedGold) {
        int currentExp = player.getCurrentExp() + exp;
        int gold = player.getGold() + addedGold;

        int maxExp = player.getMaxExp();
        int level = player.getLevel();
        boolean levelUp = false;

        Status status = statusRepository.findById(player.getId())
                .orElseThrow(NotFoundPlayerException::new);

        while (currentExp >= maxExp) {
            double multiplier = 1.15;

            if (level % 5 == 0) {
                multiplier = (level % 10 == 0 ? 1.4 : 1.25);
            }

            currentExp -= maxExp;
            maxExp *= multiplier;

            levelUpStatus(status, level);

            level++;
            player.levelUp(maxExp);
            levelUp = true;
        }

        player.killMonster(currentExp, gold);

        playerRepository.save(player);
        statusRepository.save(status);

        return LevelUpResponse.builder().levelUp(levelUp).build();
    }

    private void levelUpStatus(Status status, int level) {
        damageUp(status, level);
        healthUp(status, level);
        manaUp(status, level);
    }

    private void damageUp(Status status, int level) {
        int damageUp = 2;

        if (level != 1) {
            int exponent = (int) Math.ceil((level - 1) / 10.0);
            damageUp = (int) (Math.pow(damageUp, exponent));
        }

        status.addDamage(damageUp);
    }

    private void healthUp(Status status, int level) {
        int minus = (level <= 5 ? 1 : 0);
        int healthUp = (int) (5 + 3 * (Math.pow(2, level / 5.0) - minus));
        status.addHealth(healthUp);
    }

    private void manaUp(Status status, int level) {
        int manaUp = 5;

        if (level > 60) {
            manaUp = 10;
        } else if (level > 30) {
            manaUp = 7;
        }

        status.addMana(manaUp);
    }
}
