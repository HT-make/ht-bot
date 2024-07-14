package com.htmake.htbot.discord.commands.quest.enums;

public enum QuestButtonType {

    NEXT, BEFORE, CONFIRM;

    public static QuestButtonType toEnum(String s) {
        return QuestButtonType.valueOf(s.toUpperCase());
    }
}
