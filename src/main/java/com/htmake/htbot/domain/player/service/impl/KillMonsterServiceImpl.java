package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.presentation.data.request.KillMonsterRequest;
import com.htmake.htbot.domain.player.presentation.data.response.LevelUpResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.service.KillMonsterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KillMonsterServiceImpl implements KillMonsterService {

    private final PlayerRepository playerRepository;

    @Override
    public LevelUpResponse execute(String playerId, KillMonsterRequest request) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow();

        int currentExp = player.getCurrentExp() + Integer.parseInt(request.getExp());
        int gold = player.getGold() + Integer.parseInt(request.getGold());

        int maxExp = player.getMaxExp();
        int level = player.getLevel();

        boolean levelUp = false;

        if (currentExp >= maxExp) {
            currentExp -= maxExp;
            maxExp *= level % 5 == 0 ? 3 : 1.5;
            player.levelUp(maxExp);
            levelUp = true;
        }

        player.killMonster(currentExp, gold);

        playerRepository.save(player);

        return LevelUpResponse.builder().levelUp(levelUp).build();
    }
}
