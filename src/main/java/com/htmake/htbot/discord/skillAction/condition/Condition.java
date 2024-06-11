package com.htmake.htbot.discord.skillAction.condition;

import lombok.Getter;

@Getter
public abstract class Condition {

    protected String id;

    protected String name;

    protected String emoji;

    protected int turn;

    protected boolean check;

    protected void initialize(String id, String name, String emoji, int turn) {
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
