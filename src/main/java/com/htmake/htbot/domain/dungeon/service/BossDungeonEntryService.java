package com.htmake.htbot.domain.dungeon.service;

import com.htmake.htbot.domain.dungeon.presentation.data.response.DungeonResponse;

public interface BossDungeonEntryService {

    DungeonResponse execute(String dungeonId, String playerId);
}
