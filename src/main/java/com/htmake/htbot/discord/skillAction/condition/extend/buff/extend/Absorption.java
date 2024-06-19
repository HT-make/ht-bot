package com.htmake.htbot.discord.skillAction.condition.extend.buff.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.type.BuffType;

public class Absorption extends Buff {

    public Absorption(int version) {
        super("absorption", "흡수" + getRomanNumeral(version), ":nazar_amulet:", 3, getValue(version));
    }

    private static String getRomanNumeral(int version) {
        return switch (version) {
            case 1 -> "I";
            case 2 -> "II";
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }

    private static double getValue(int version) {
        return switch (version) {
            case 1 -> 0.1;
            case 2 -> 0.15;
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
        int valueChange = (int) (value * isBuff * originalStatus.getHealth());
        status.setDefence(Math.max(0, currentValue + valueChange));
    }
}
