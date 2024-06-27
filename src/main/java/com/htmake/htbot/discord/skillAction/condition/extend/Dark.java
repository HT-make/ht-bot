package com.htmake.htbot.discord.skillAction.condition.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;

public class Dark extends Condition {

    public Dark() {
        initialize("dark", "어둠", ":black_large_square:", 3);
    }

    public void apply(BasicStatus status, BasicStatus originalStatus) {
        applyBuff(status, originalStatus, -1);
    }

    public void unapply(BasicStatus status, BasicStatus originalStatus) {
        applyBuff(status, originalStatus, 1);
    }

    public void applyBuff(BasicStatus status, BasicStatus originalStatus, int multiplier) {
        int currentDamage = status.getDamage();
        int currentDefence = status.getDefence();
        int damageChange = (int) (originalStatus.getDamage() * 0.3 * multiplier);
        int defenceChange = (int) (originalStatus.getDefence() * 0.1 * multiplier);

        status.setDamage(currentDamage + damageChange);
        status.setDefence(currentDefence + defenceChange);
    }
}
