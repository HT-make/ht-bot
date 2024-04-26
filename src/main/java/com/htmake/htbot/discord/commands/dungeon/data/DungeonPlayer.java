package com.htmake.htbot.discord.commands.dungeon.data;

import com.htmake.htbot.discord.commands.battle.data.PlayerSkillStatus;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DungeonPlayer {

    private int level;

    private int damage;

    private int health;

    private int defence;

    private int mana;

    private int criticalChance;

    private int criticalDamage;

    private Map<Integer, PlayerSkillStatus> playerSkill;
}
