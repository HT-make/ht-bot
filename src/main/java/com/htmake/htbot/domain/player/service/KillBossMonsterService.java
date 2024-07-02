package com.htmake.htbot.domain.player.service;

import com.htmake.htbot.domain.player.presentation.data.request.KillBossMonsterRequest;
import com.htmake.htbot.domain.player.presentation.data.response.LevelUpResponse;

public interface KillBossMonsterService {
    LevelUpResponse execute(String playerId, KillBossMonsterRequest request);
}
