package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.player.presentation.data.response.PlayerJoinCheckResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.service.PlayerJoinCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
