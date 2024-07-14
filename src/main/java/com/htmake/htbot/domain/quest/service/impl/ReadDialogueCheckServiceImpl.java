package com.htmake.htbot.domain.quest.service.impl;

import com.htmake.htbot.domain.quest.entity.PlayerQuest;
import com.htmake.htbot.domain.quest.exception.NotFoundQuestException;
import com.htmake.htbot.domain.quest.presentation.data.response.ReadDialogueCheckResponse;
import com.htmake.htbot.domain.quest.repository.PlayerQuestRepository;
import com.htmake.htbot.domain.quest.service.ReadDialogueCheckService;
import com.htmake.htbot.global.annotation.ReadOnlyService;
import lombok.RequiredArgsConstructor;

@ReadOnlyService
@RequiredArgsConstructor
public class ReadDialogueCheckServiceImpl implements ReadDialogueCheckService {

    private final PlayerQuestRepository playerQuestRepository;

    @Override
    public ReadDialogueCheckResponse execute(String playerId) {
        PlayerQuest playerQuest = playerQuestRepository.findByPlayerId(playerId)
                .orElseThrow(NotFoundQuestException::new);

        return ReadDialogueCheckResponse.builder()
                .readDialogue(playerQuest.isReadDialogue())
                .build();
    }
}
