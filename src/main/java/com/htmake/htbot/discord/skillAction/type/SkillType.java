package com.htmake.htbot.discord.skillAction.type;

public enum SkillType {

    ATTACK, HEAL, BUFF, DEBUFF;

    public static SkillType toSkillType(String s) {
        return SkillType.valueOf(s.toUpperCase());
    }
}
