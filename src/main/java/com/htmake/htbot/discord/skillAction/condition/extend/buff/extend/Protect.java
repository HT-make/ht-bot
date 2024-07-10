package com.htmake.htbot.discord.skillAction.condition.extend.buff.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.type.BuffType;

public class Protect extends Buff {

    public Protect(int version) {
        super("protect", "보호" + getRomanNumeral(version), ":shield:", 3, getValue(version));
    }

    private static double getValue(int version) {
        return switch (version) {
            case 1 -> 0.5;
            case 2 -> 0.7;
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }

    @Override
    protected BuffType getBuffType() {
        return BuffType.UP;
    }

    @Override
    protected void applyBuff(BasicStatus status, BasicStatus originalStatus, int isBuff) {
        int currentValue = status.getDefence();
        int originalValue = originalStatus.getDefence();
        int valueChange = (int) (originalValue * value * isBuff);
        status.setDefence(Math.max(0, currentValue + valueChange));
    }
}
