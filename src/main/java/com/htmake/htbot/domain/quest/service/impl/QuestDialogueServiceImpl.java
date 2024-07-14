package com.htmake.htbot.domain.quest.service.impl;

import com.htmake.htbot.domain.quest.entity.MainQuest;
import com.htmake.htbot.domain.quest.entity.PlayerQuest;
import com.htmake.htbot.domain.quest.entity.dialogue.QuestDialogue;
import com.htmake.htbot.domain.quest.enums.Trigger;
import com.htmake.htbot.domain.quest.exception.NotFoundQuestException;
import com.htmake.htbot.domain.quest.presentation.data.response.DialogueDetailResponse;
import com.htmake.htbot.domain.quest.presentation.data.response.QuestDialogueResponse;
import com.htmake.htbot.domain.quest.repository.PlayerQuestRepository;
import com.htmake.htbot.domain.quest.repository.QuestDialogueRepository;
import com.htmake.htbot.domain.quest.service.QuestDialogueService;
import com.htmake.htbot.global.annotation.ReadOnlyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@ReadOnlyService
@RequiredArgsConstructor
public class QuestDialogueServiceImpl implements QuestDialogueService {

    private final PlayerQuestRepository playerQuestRepository;
    private final QuestDialogueRepository questDialogueRepository;

    @Override
    public QuestDialogueResponse execute(String playerId, String trigger) {
        PlayerQuest playerQuest = playerQuestRepository.findByPlayerId(playerId)
                .orElseThrow(NotFoundQuestException::new);

        MainQuest mainQuest = playerQuest.getMainQuest();
        Trigger enumTrigger = Trigger.valueOf(trigger);
        Sort sort = Sort.by(Sort.Direction.ASC, "sequence");

        List<QuestDialogue> questDialogueList = questDialogueRepository
                .findByMainQuestAndTrigger(mainQuest, enumTrigger, sort);

        return QuestDialogueResponse.builder()
                .dialogueList(
                        questDialogueList.stream()
                                .map(DialogueDetailResponse::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
