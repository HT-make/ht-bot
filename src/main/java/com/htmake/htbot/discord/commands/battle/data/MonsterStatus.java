package com.htmake.htbot.discord.commands.battle.data;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonsterStatus {

    private String id;

    private String name;

    private int damage;

    private int health;

    private int defence;
}
