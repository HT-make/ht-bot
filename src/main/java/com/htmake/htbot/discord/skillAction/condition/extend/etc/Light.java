package com.htmake.htbot.discord.skillAction.condition.extend.etc;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;

public class Light extends Condition {

    public Light() {
        initialize("light", "빛", ":sparkles:", 0);
    }

    public void apply(BasicStatus status, BasicStatus originalStatus) {
        applyBuff(status, originalStatus);
    }

    public void unapply(BasicStatus status, BasicStatus originalStatus, int stack) {
        unapplyBuff(status, originalStatus, stack);
    }

    private void applyBuff(BasicStatus status, BasicStatus originalStatus) {
        turn++;

        int currentDamage = status.getDamage();
        int currentDefence = status.getDefence();
        int damageChange = (int) (originalStatus.getDamage() * 0.1);
        int defenceChange = (int) (originalStatus.getDefence() * 0.14);

        status.setDamage(currentDamage + damageChange);
        status.setDefence(currentDefence + defenceChange);
    }

    private void unapplyBuff(BasicStatus status, BasicStatus originalStatus, int stack) {
        turn -= stack;

        int currentDamage = status.getDamage();
        int currentDefence = status.getDefence();
        int damageChange = (int) (originalStatus.getDamage() * 0.1 * stack);
        int defenceChange = (int) (originalStatus.getDefence() * 0.14 * stack);

        status.setDamage(currentDamage - damageChange);
        status.setDefence(currentDefence - defenceChange);
    }
}
