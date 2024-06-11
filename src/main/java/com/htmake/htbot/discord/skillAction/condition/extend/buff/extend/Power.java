package com.htmake.htbot.discord.skillAction.condition.extend.buff.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.type.BuffType;

public class Power extends Buff {

    public Power(int version) {
        super("power", "힘" + getRomanNumeral(version), ":crossed_swords:", getTurn(version), getValue(version));
    }

    private static String getRomanNumeral(int version) {
        return switch (version) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }

    private static int getTurn(int version) {
        return switch (version) {
            case 1, 2 -> 3;
            case 3 -> 2;
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }

    private static double getValue(int version) {
        return switch (version) {
            case 1 -> 0.3;
            case 2 -> 0.5;
            case 3 -> 0.7;
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }

    @Override
    protected BuffType getBuffType() {
        return BuffType.UP;
    }

    @Override
    protected void applyBuff(BasicStatus status, BasicStatus originalStatus, int isBuff) {
        int currentValue = status.getDamage();
        int originalValue = originalStatus.getDamage();
        int valueChange = (int) (originalValue * value * isBuff);
        status.setDamage(Math.max(0, currentValue + valueChange));
    }
}
