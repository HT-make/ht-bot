package com.htmake.htbot.discord.commands.quest.data;

import com.htmake.htbot.domain.quest.enums.Trigger;
import com.htmake.htbot.global.generics.LimitedCapacityDeque;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestDialogueData {

    private List<DialogueData> dialogueList;

    private LimitedCapacityDeque<DialogueData> ongoingDialogue;

    private Trigger trigger;
}
