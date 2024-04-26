package com.htmake.htbot.discord.commands.battle.data;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStatus {

    private int damage;

    private int health;

    private int defence;

    private int mana;

    private int criticalChance;

    private int criticalDamage;

    private Map<Integer, PlayerSkillStatus> playerSkill;
}
