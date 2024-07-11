package com.htmake.htbot.discord.skillAction.condition.extend.buff.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.type.BuffType;

public class Blind extends Buff {

    public Blind(int version) {
        super("blind", "실명" + getRomanNumeral(version), ":eye:", 3, getValue(version));
    }

    private static double getValue(int version) {
        return switch (version) {
            case 1 -> 0.2;
            case 2 -> 0.3;
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }

    @Override
    protected BuffType getBuffType() {
        return BuffType.DOWN;
    }

    @Override
    protected void applyBuff(BasicStatus status, BasicStatus originalStatus, int isBuff) {
        int currentValue = status.getCriticalChance();
        int valueChange = (int) (value * isBuff * 100);
        status.setCriticalChance(Math.max(0, currentValue + valueChange));
    }
}
