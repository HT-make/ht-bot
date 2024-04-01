package com.htmake.htbot.domain.player.service;

import com.htmake.htbot.domain.player.presentation.data.response.PlayerBattleResponse;

public interface PlayerBattleService {

    PlayerBattleResponse execute(String playerId);
}
