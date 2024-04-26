package com.htmake.htbot.discord.commands.battle.data;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerSkillStatus {

    private String name;

    private int value;

    private int mana;

    private String skillType;
}
