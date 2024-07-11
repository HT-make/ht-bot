package com.htmake.htbot.discord.skillAction.condition.extend.buff.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.type.BuffType;

public class PerfectAccuracy extends Buff {

    public PerfectAccuracy() {
        super("perfect_accuracy", "백발백중", ":bow_and_arrow:", 2, 100);
    }

    @Override
    protected BuffType getBuffType() {
        return BuffType.UP;
    }

    @Override
    protected void applyBuff(BasicStatus status, BasicStatus originalStatus, int isBuff) {
        int currentCriticalChance = status.getCriticalChance();
        int currentCriticalDamage = status.getCriticalDamage();
        int valueChange = (int) (value * isBuff);
        status.setCriticalChance(Math.max(0, currentCriticalChance + valueChange));
        status.setCriticalDamage(Math.max(0, currentCriticalDamage + valueChange));
    }
}
