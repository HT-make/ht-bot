package com.htmake.htbot.discord.skillAction.condition.extend.etc;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;

public class LifeSteal extends Condition {

    private double value;

    public LifeSteal(int version) {
        initialize("life_steal", "흡혈" + getRomanNumeral(version), ":bat:", 3);
        setValue(version);
    }

    private void setValue(int version) {
        switch (version) {
            case 1 -> this.value = 0.3;
            case 2 -> this.value = 0.5;
        }
    }

    public void applyEffect(BasicStatus status, BasicStatus originalStatus, int damage) {
        int currentHealth = status.getHealth();
        int maxHealth = originalStatus.getHealth();
        int healing = (int) (damage * value);
        status.setHealth(Math.min(maxHealth, currentHealth + healing));
    }
}
