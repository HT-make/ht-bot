package com.htmake.htbot.domain.player.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.presentation.data.request.PlayerJoinRequest;
import com.htmake.htbot.domain.player.presentation.data.response.PlayerJoinResponse;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.player.service.PlayerJoinService;
import com.htmake.htbot.domain.player.entity.Status;
import com.htmake.htbot.domain.player.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlayerJoinServiceImpl implements PlayerJoinService {

    private final PlayerRepository playerRepository;
    private final StatusRepository statusRepository;

    @Override
    public PlayerJoinResponse execute(PlayerJoinRequest request) {

        String message;

        if (playerRepository.existsById(request.getUserId())) {
            message = "이미 가입한 유저입니다.";
        } else {
            playerRepository.save(
                    Player.builder()
                            .id(request.getUserId())
                            .name(request.getName())
                            .level(1)
                            .currentExp(0)
                            .maxExp(100)
                            .build()
            );

            statusRepository.save(
                    Status.builder()
                            .id(request.getUserId())
                            .damage(10)
                            .health(100)
                            .defence(5)
                            .mana(100)
                            .criticalChance(10)
                            .criticalDamage(200)
                            .build()
            );

            message = "가입에 성공했습니다!";
        }

        return PlayerJoinResponse.builder()
                .message(message)
                .build();
    }
}
