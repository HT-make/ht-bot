package com.htmake.htbot.domain.player.service;

import com.htmake.htbot.domain.player.presentation.data.response.PlayerJoinCheckResponse;

public interface PlayerJoinCheckService {

    PlayerJoinCheckResponse execute(String playerId);
}
