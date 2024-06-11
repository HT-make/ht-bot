package com.htmake.htbot.discord.skillAction.condition.extend.buff.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.type.BuffType;

public class Hit extends Buff {

    public Hit(int version) {
        super("hit", "명중" + getRomanNumeral(version), ":dart:", 3, getValue(version));
    }

    private static String getRomanNumeral(int version) {
        return switch (version) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }

    private static double getValue(int version) {
        return switch (version) {
            case 1 -> 0.15;
            case 2 -> 0.25;
            case 3 -> 0.4;
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }

    @Override
    protected BuffType getBuffType() {
        return BuffType.UP;
    }

    @Override
    protected void applyBuff(BasicStatus status, BasicStatus originalStatus, int isBuff) {
        int currentValue = status.getCriticalChance();
        int valueChange = (int) (value * isBuff * 100);
        status.setCriticalChance(Math.max(0, currentValue + valueChange));
    }
}
