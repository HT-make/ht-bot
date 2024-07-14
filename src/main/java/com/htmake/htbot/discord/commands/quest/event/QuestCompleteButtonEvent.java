package com.htmake.htbot.discord.commands.quest.event;

import com.htmake.htbot.discord.commands.quest.action.QuestDialogueAction;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class QuestCompleteButtonEvent {

    private final QuestDialogueAction questDialogueAction;

    public QuestCompleteButtonEvent() {
        this.questDialogueAction = new QuestDialogueAction();
    }

    public void execute(ButtonInteractionEvent event) {
        questDialogueAction.execute(event);
    }
}
