package com.htmake.htbot.domain.dungeon.service;

import com.htmake.htbot.domain.dungeon.presentation.data.response.MonsterLootResponse;

public interface MonsterLootService {

    MonsterLootResponse execute(String monsterName);
}
