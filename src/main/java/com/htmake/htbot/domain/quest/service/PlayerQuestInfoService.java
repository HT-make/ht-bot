package com.htmake.htbot.domain.quest.service;

import com.htmake.htbot.domain.quest.presentation.data.response.PlayerQuestInfoResponse;

public interface PlayerQuestInfoService {
    PlayerQuestInfoResponse execute(String playerId);
}
