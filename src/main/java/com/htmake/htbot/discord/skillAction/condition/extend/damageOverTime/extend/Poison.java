package com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.extend;

import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.DamageOverTime;

public class Poison extends DamageOverTime {

    public Poison(int version) {
        super("poison", "독" + getRomanNumeral(version), ":skull_crossbones:", 3, getValue(version));
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
            case 1 -> 0.05;
            case 2 -> 0.07;
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }
}
