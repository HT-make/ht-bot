package com.htmake.htbot.discord.commands.battle.data.condition.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import com.htmake.htbot.discord.commands.battle.data.condition.Condition;
import lombok.Getter;

@Getter
public class DamageOverTime extends Condition {

    private final int damage;

    public DamageOverTime(String id, String name, String emoji, int turn, int damage) {
        super(id, name, emoji, turn);
        this.damage = damage;
    }

    public void execute(BasicStatus status) {
        status.setHealth(Math.max(0, status.getHealth() - damage));
    }
}
