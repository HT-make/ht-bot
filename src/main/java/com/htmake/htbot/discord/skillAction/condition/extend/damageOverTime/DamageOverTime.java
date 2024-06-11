package com.htmake.htbot.discord.skillAction.condition.extend.damageOverTime;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.skillAction.condition.Condition;

public abstract class DamageOverTime extends Condition {

    protected double value;

    private int effectDamage;

    public DamageOverTime(String id, String name, String emoji, int turn, double value) {
        initialize(id, name, emoji, turn);
        this.value = value;
    }

    public int getEffectDamage() {
        return this.effectDamage;
    }

    public void setEffectDamage(int damage) {
        this.effectDamage = (int) (damage * value);
    }

    public void applyDamage(BasicStatus status) {
        status.setHealth(Math.max(0, status.getHealth() - getEffectDamage()));
    }
}
