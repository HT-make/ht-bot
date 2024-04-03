package com.htmake.htbot.domain.player.service;

import com.htmake.htbot.domain.player.presentation.data.request.KillMonsterRequest;
import com.htmake.htbot.domain.player.presentation.data.response.LevelUpResponse;

public interface KillMonsterService {

    LevelUpResponse execute(String playerId, KillMonsterRequest request);
}
