package com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.extend;

import com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime.DamageOverTime;

public class Toxic extends DamageOverTime {

    public Toxic(int version) {
        super("toxic", "맹독" + getRomanNumeral(version), ":skull_crossbones:", 2, getValue(version));
    }

    private static double getValue(int version) {
        return switch (version) {
            case 1 -> 0.2;
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        };
    }
}
