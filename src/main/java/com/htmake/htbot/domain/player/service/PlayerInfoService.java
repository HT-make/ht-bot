package com.htmake.htbot.domain.player.service;

import com.htmake.htbot.domain.player.presentation.data.response.PlayerInfoResponse;

public interface PlayerInfoService {

    PlayerInfoResponse execute(String playerId);
}
