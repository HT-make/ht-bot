package com.htmake.htbot.domain.quest.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestDialogueResponse {

    private List<DialogueDetailResponse> dialogueList;
}
