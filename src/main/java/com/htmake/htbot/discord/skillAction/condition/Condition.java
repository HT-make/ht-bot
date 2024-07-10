package com.htmake.htbot.discord.skillAction.condition;

import lombok.Getter;

@Getter
public abstract class Condition implements Cloneable {

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

    public static String getRomanNumeral(int version) {
        return switch (version) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
