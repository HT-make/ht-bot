package com.htmake.htbot.domain.dungeon.service;

import com.htmake.htbot.domain.dungeon.presentation.data.response.DungeonResponse;

public interface DungeonService {

    DungeonResponse execute(String dungeonId);
}
