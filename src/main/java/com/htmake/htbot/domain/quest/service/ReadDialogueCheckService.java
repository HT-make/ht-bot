package com.htmake.htbot.domain.quest.service;

import com.htmake.htbot.domain.quest.presentation.data.response.ReadDialogueCheckResponse;

public interface ReadDialogueCheckService {

    ReadDialogueCheckResponse execute(String playerId);
}
