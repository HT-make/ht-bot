package com.htmake.htbot.domain.monster.service;

import com.htmake.htbot.domain.monster.presentation.data.response.MonsterLootResponse;

public interface MonsterLootService {

    MonsterLootResponse execute(String monsterId);
}
