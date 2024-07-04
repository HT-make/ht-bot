package com.htmake.htbot.domain.dungeon.presentation.data.response;

import com.htmake.htbot.discord.skillAction.type.SkillType;
import com.htmake.htbot.domain.monster.entity.MonsterSkill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonsterSkillResponse {

    private String name;

    private int damage;

    private String effect;

    private int chance;

    private SkillType skillType;

    public static MonsterSkillResponse toResponse(MonsterSkill monsterSkill) {
        String effect = "";

        if (monsterSkill.getEffect() != null) {
            effect = monsterSkill.getEffect();
        }

        return MonsterSkillResponse.builder()
                .name(monsterSkill.getName())
                .damage(monsterSkill.getDamage())
                .effect(effect)
                .chance(monsterSkill.getChance())
                .skillType(monsterSkill.getSkillType())
                .build();
    }
}
