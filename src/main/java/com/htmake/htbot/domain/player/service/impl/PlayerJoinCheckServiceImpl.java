package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.player.presentation.data.response.PlayerJoinCheckResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.service.PlayerJoinCheckService;
import com.htmake.htbot.global.annotation.ReadOnlyService;
import lombok.RequiredArgsConstructor;

@ReadOnlyService
@RequiredArgsConstructor
public class PlayerJoinCheckServiceImpl implements PlayerJoinCheckService {

    private final PlayerRepository playerRepository;

    @Override
    public PlayerJoinCheckResponse execute(String playerId) {
        boolean exists = playerRepository.existsById(playerId);

        return PlayerJoinCheckResponse.builder()
                .exists(String.valueOf(exists))
                .build();
    }
}
