package com.htmake.htbot.domain.quest.service;

import com.htmake.htbot.domain.quest.presentation.data.request.PlayerQuestMonsterQuantityRequest;

public interface PlayerQuestMonsterQuantityService {
    void execute(String playerId, PlayerQuestMonsterQuantityRequest request);
}
