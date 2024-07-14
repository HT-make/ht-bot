package com.htmake.htbot.discord.commands.quest.util;

import com.htmake.htbot.discord.commands.quest.data.DialogueData;

import java.util.List;

public class QuestUtil {

    public String convertDialogue(List<DialogueData> dialogueList) {
        StringBuilder sb = new StringBuilder();

        for (DialogueData dialogue : dialogueList) {
            sb.append(dialogueFormat(dialogue)).append("\n\n");
        }

        return sb.toString();
    }

    private String dialogueFormat(DialogueData dialogue) {
        return dialogue.getCharacter() + ": " + dialogue.getDialogue();
    }
}
