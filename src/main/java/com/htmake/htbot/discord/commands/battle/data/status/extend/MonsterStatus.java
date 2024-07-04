package com.htmake.htbot.discord.commands.battle.data.status.extend;

import com.htmake.htbot.discord.commands.battle.data.MonsterSkillData;
import com.htmake.htbot.discord.commands.battle.data.status.BasicStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class MonsterStatus extends BasicStatus {

    private String id;

    private int skillChance;

    private List<MonsterSkillData> skillList;

    public MonsterStatus(
            int level,
            String name,
            int damage,
            int health,
            int defence,
            int criticalChance,
            int criticalDamage,
            String id,
            int skillChance,
            List<MonsterSkillData> skillList
    ) {
        super(level, name, damage, health, defence, criticalChance, criticalDamage);
        this.id = id;
        this.skillChance = skillChance;
        this.skillList = skillList;
    }
}
