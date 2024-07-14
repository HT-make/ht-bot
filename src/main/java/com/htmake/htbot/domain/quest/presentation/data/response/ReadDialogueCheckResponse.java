package com.htmake.htbot.domain.quest.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadDialogueCheckResponse {

    private boolean readDialogue;
}
