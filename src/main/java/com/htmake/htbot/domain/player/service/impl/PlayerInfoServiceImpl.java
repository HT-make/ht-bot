package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.presentation.data.response.PlayerInfoResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.service.PlayerInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlayerInfoServiceImpl implements PlayerInfoService {

    private final PlayerRepository playerRepository;

    @Override
    public PlayerInfoResponse execute(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow();

        return PlayerInfoResponse.builder()
                .level(player.getLevel())
                .currentExp(player.getCurrentExp())
                .maxExp(player.getMaxExp())
                .build();
    }
}
