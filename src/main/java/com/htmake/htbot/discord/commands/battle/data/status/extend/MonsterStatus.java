package com.htmake.htbot.discord.commands.battle.data.status.extend;

import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import lombok.*;

@Getter
@Setter
public class MonsterStatus extends BasicStatus {

    private String id;

    private String skillName;

    private int skillDamage;

    public MonsterStatus(int level, String name, int damage, int health, int defence, int criticalChance, int criticalDamage, String id, String skillName, int skillDamage) {
        super(level, name, damage, health, defence, criticalChance, criticalDamage);
        this.id = id;
        this.skillName = skillName;
        this.skillDamage = skillDamage;
    }
}
