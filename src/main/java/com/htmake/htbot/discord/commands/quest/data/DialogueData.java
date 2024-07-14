package com.htmake.htbot.discord.commands.quest.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DialogueData {

    private String character;

    private String dialogue;

    private int sequence;

    public void setPlayerName(String playerName) {
        if (character.equals("{player}")) character = playerName;
        dialogue = dialogue.replace("{player}", playerName);
    }
}
