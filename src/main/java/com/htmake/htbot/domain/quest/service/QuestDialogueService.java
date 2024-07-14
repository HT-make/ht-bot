package com.htmake.htbot.domain.quest.service;

import com.htmake.htbot.domain.quest.presentation.data.response.QuestDialogueResponse;

public interface QuestDialogueService {

    QuestDialogueResponse execute(String playerId, String trigger);
}
