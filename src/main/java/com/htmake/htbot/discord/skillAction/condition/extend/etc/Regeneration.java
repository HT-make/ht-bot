package com.htmake.htbot.discord.skillAction.condition.extend.etc;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;

public class Regeneration extends Condition {

    private double value;

    public Regeneration(int version) {
        initialize("regeneration", "재생" + getRomanNumeral(version), ":sparkling_heart:", 3);
        setValue(version);
    }

    private void setValue(int version) {
        switch (version) {
            case 1 -> this.value = 0.05;
            case 2 -> this.value = 0.15;
        }
    }

    public void applyEffect(BasicStatus status, BasicStatus originalStatus) {
        int currentHealth = status.getHealth();
        int maxHealth = originalStatus.getHealth();
        int healing = (int) (maxHealth * value);
        status.setHealth(Math.min(maxHealth, currentHealth + healing));
    }
}
