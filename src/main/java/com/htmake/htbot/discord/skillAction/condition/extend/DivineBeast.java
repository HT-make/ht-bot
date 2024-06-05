package com.htmake.htbot.discord.skillAction.condition.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;

public class DivineBeast extends Condition {

    public DivineBeast(String id, String name, String emoji, int turn) {
        super(id, name, emoji, turn);
    }

    public void apply(BasicStatus status, BasicStatus originalStatus) {
        applyBuff(status, originalStatus, 1);
    }

    public void unapply(BasicStatus status, BasicStatus originalStatus) {
        applyBuff(status, originalStatus, -1);
    }

    public void applyBuff(BasicStatus status, BasicStatus originalStatus, int multiplier) {
        int currentDamage = status.getDamage();
        int currentDefence = status.getDefence();
        int damageChange = (int) (originalStatus.getDamage() * 0.4 * multiplier);
        int defenceChange = (int) (originalStatus.getDefence() * 0.3 * multiplier);

        status.setDamage(currentDamage + damageChange);
        status.setDefence(currentDefence + defenceChange);
    }

    public void heal(BasicStatus status, BasicStatus originalStatus) {
        int currentHealth = status.getHealth();
        int maxHealth = originalStatus.getHealth();
        int healing = (int) (maxHealth * 0.04);

        status.setHealth(Math.max(maxHealth, currentHealth + healing));
    }
}
