package com.htmake.htbot.discord.skillAction.condition.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;

public class SwordGod extends Condition {

    private double value;

    public SwordGod() {
        initialize("sword_god", "검신", ":star_and_crescent:", 5);
        value = 0.4;
    }

    public void apply(BasicStatus status, BasicStatus originalStatus) {
        applyBuff(status, originalStatus, 1);
    }

    public void unapply(BasicStatus status, BasicStatus originalStatus) {
        applyBuff(status, originalStatus, -1);
    }

    private void applyBuff(BasicStatus status, BasicStatus originalStatus, int multiplier) {
        int currentDamage = status.getDamage();
        int damageChange = (int) (originalStatus.getDamage() * value * multiplier);

        status.setDamage(currentDamage + damageChange);
    }

    public void updateBuff(BasicStatus status, BasicStatus originalStatus) {
        unapply(status, originalStatus);
        value += 0.08;
        apply(status, originalStatus);
    }
}
