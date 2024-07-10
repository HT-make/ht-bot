package com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.extend;

import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.DamageOverTime;

public class Fire extends DamageOverTime {

    public Fire(int version) {
        super("fire", "화상" + getRomanNumeral(version), ":fire:", 3, getValue(version));
    }

    private static double getValue(int version) {
        return switch (version) {
            case 1 -> 0.5;
            case 2 -> 0.9;
            case 3 -> 1.5;
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }
}
