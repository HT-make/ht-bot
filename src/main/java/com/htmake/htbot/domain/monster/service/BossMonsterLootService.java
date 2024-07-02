package com.htmake.htbot.domain.monster.service;

import com.htmake.htbot.domain.monster.presentation.data.response.BossMonsterLootResponse;

public interface BossMonsterLootService {

    BossMonsterLootResponse execute(String bossId);
}
