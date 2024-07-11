package com.htmake.htbot.discord.skillAction.condition.extend.buff.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.extend.buff.Buff;
import com.htmake.htbot.discord.skillAction.type.BuffType;

public class Frostbite extends Buff {

    public Frostbite(int version) {
        super("frostbite", "동상" + getRomanNumeral(version), ":snowflake:", 2, getValue(version));
    }

    private static double getValue(int version) {
        return switch (version) {
            case 1 -> 0.1;
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
        int currentDamage = status.getDamage();
        int originalDamage = originalStatus.getDamage();
        int damageChange = (int) (originalDamage * value * isBuff);
        status.setDamage(Math.max(0, currentDamage + damageChange));
    }
}
