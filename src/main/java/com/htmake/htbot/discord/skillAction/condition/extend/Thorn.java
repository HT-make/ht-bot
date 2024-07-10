package com.htmake.htbot.discord.skillAction.condition.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;

public class Thorn extends Condition {

    private double value;

    public Thorn(int version) {
        initialize("thorn", "가시" + getRomanNumeral(version), ":sewing_needle:", 3);
        setValue(version);
    }

    private void setValue(int version) {
        switch (version) {
            case 1 -> this.value = 0.1;
            case 2 -> this.value = 0.2;
        }
    }

    public void applyEffect(BasicStatus status, int damage) {
        int effectDamage = (int) (damage * value);
        status.setHealth(Math.max(0, status.getHealth() - effectDamage));
    }
}
