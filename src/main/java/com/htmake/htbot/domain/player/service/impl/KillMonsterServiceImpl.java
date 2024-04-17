package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.presentation.data.request.KillMonsterRequest;
import com.htmake.htbot.domain.player.presentation.data.response.LevelUpResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.service.KillMonsterService;
import com.htmake.htbot.global.util.PlayerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class KillMonsterServiceImpl implements KillMonsterService {

    private final PlayerRepository playerRepository;
    private final PlayerUtil playerUtil;

    @Override
    public LevelUpResponse execute(String playerId, KillMonsterRequest request) {

        Player player = playerRepository.findById(playerId)
                .orElseThrow();

        int exp = request.getExp();
        int gold = request.getGold();

        return playerUtil.executeLevelUp(player, exp, gold);
    }
}
