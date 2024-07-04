package com.htmake.htbot.discord.commands.battle.data;

import com.htmake.htbot.discord.skillAction.type.SkillType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonsterSkillData {

    private String name;

    private int damage;

    private String effect;

    private int chance;

    private SkillType skillType;
}
