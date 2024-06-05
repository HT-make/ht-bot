package com.htmake.htbot.discord.skillAction.condition.extend;

import com.htmake.htbot.discord.skillAction.condition.Condition;
import lombok.Getter;

@Getter
public class Faint extends Condition {

    private final int value;

    public Faint(String id, String name, String emoji, int turn, int value) {
        super(id, name, emoji, turn);
        this.value = value;
    }
}
