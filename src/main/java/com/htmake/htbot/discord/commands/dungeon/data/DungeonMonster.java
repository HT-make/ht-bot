package com.htmake.htbot.discord.commands.dungeon.data;

import lombok.*;

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

    private String skillName;

    private int skillDamage;
}
