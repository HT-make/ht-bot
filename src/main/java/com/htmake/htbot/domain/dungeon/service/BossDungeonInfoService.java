package com.htmake.htbot.domain.dungeon.service;

import com.htmake.htbot.domain.dungeon.presentation.data.response.BossDungeonInfoResponse;

public interface BossDungeonInfoService {

    BossDungeonInfoResponse execute(String dungeonId, String playerId);
}
