package com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.extend;

import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.DamageOverTime;

public class Bleeding extends DamageOverTime {

    public Bleeding(int version) {
        super("bleeding", "출혈" + getRomanNumeral(version), ":drop_of_blood:", 5, getValue(version));
    }

    private static double getValue(int version) {
        return switch (version) {
            case 1 -> 0.35;
            case 2 -> 0.7;
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }
}
