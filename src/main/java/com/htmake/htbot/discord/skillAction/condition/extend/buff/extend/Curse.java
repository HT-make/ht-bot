package com.htmake.htbot.discord.skillAction.condition.extend.buff.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.type.BuffType;

public class Curse extends Buff {

    public Curse(int version) {
        super("curse", "저주" + getRomanNumeral(version), ":smiling_imp:", 3, getValue(version));
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
            case 1 -> 0.2;
            case 2 -> 0.4;
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }

    @Override
    protected BuffType getBuffType() {
        return BuffType.DOWN;
    }

    @Override
    protected void applyBuff(BasicStatus status, BasicStatus originalStatus, int isBuff) {
        int currentDefence = status.getDefence();
        int originalDefence = originalStatus.getDefence();
        int defenceChange = (int) (originalDefence * value * isBuff);
        status.setDefence(Math.max(0, currentDefence + defenceChange));
    }
}
