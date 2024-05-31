package com.htmake.htbot.discord.commands.battle.data.status.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import lombok.Getter;

@Getter
public class PlayerOriginalStatus extends BasicStatus {
    public PlayerOriginalStatus(int level, String name, int damage, int health, int defence, int criticalChance, int criticalDamage) {
        super(level, name, damage, health, defence, criticalChance, criticalDamage);
    }
}
