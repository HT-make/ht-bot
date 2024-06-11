package com.htmake.htbot.discord.skillAction.condition.extend.buff;

import com.htmake.htbot.discord.skillAction.condition.Condition;
import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.type.BuffType;

public abstract class Buff extends Condition {

    protected double value;

    public Buff(String id, String name, String emoji, int turn, double value) {
        initialize(id, name, emoji, turn);
        this.value = value;
    }

    protected abstract BuffType getBuffType();

    protected abstract void applyBuff(BasicStatus status, BasicStatus originalStatus, int isBuff);

    public void apply(BasicStatus status, BasicStatus originalStatus) {
        int buffMultiplier = (getBuffType() == BuffType.UP ? 1 : -1);
        applyBuff(status, originalStatus, buffMultiplier);
    }

    public void unapply(BasicStatus status, BasicStatus originalStatus) {
        int buffMultiplier = (getBuffType() == BuffType.UP ? -1 : 1);
        applyBuff(status, originalStatus, buffMultiplier);
    }
}
