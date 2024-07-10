package com.htmake.htbot.discord.commands.dungeon.data;

import com.htmake.htbot.discord.commands.battle.data.MonsterSkillData;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DungeonMonster {

    private String id;

    private String name;

    private int level;

    private int damage;

    private int health;

    private int defence;

    private int criticalChance;

    private int criticalDamage;

    private int skillChance;

    private List<MonsterSkillData> skillList;
}
