package com.htmake.htbot.domain.quest.presentation.data.response;

import com.htmake.htbot.domain.quest.entity.dialogue.QuestDialogue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DialogueDetailResponse {

    private String character;

    private String dialogue;

    private int sequence;

    public static DialogueDetailResponse toResponse(QuestDialogue questDialogue) {
        return DialogueDetailResponse.builder()
                .character(questDialogue.getCharacter())
                .dialogue(questDialogue.getDialogue())
                .sequence(questDialogue.getSequence())
                .build();
    }
}
