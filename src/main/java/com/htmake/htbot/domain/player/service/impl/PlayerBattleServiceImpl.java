package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.entity.Status;
import com.htmake.htbot.domain.player.presentation.data.response.PlayerBattleResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.repository.StatusRepository;
import com.htmake.htbot.domain.player.service.PlayerBattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlayerBattleServiceImpl implements PlayerBattleService {

    private final PlayerRepository playerRepository;

    private final StatusRepository statusRepository;

    @Override
    public PlayerBattleResponse execute(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(RuntimeException::new);

        Status status = statusRepository.findById(playerId)
                .orElseThrow(RuntimeException::new);

        return PlayerBattleResponse.builder()
                .level(player.getLevel())
                .damage(status.getDamage())
                .health(status.getHealth())
                .defence(status.getDefence())
                .mana(status.getMana())
                .criticalChance(status.getCriticalChance())
                .criticalDamage(status.getCriticalDamage())
                .build();
    }
}
