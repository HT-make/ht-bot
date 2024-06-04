package com.htmake.htbot.discord.skillAction.condition;

import lombok.Getter;

@Getter
public abstract class Condition {

    private final String id;

    private final String name;

    private final String emoji;

    private int turn;

    private boolean check;

    public Condition(String id, String name, String emoji, int turn) {
        this.id = id;
        this.name = name;
        this.emoji = emoji;
        this.turn = turn;
        this.check = false;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
