package com.htmake.htbot.domain.dungeon.presentation.data.response;

import com.htmake.htbot.domain.monster.entity.Monster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonsterResponse {

    private String id;

    private String name;

    private int level;

    private int damage;

    private int health;

    private int defence;

    private String skillName;

    private int skillDamage;

    public static MonsterResponse toResponse(Monster monster) {
        MonsterResponse.MonsterResponseBuilder responseBuilder = MonsterResponse.builder()
                .id(monster.getId())
                .name(monster.getName())
                .level(monster.getLevel())
                .damage(monster.getDamage())
                .health(monster.getHealth())
                .defence(monster.getDefence());

        if (monster.getMonsterSkill() != null) {
            responseBuilder
                    .skillName(monster.getMonsterSkill().getName())
                    .skillDamage(monster.getMonsterSkill().getDamage());
        } else {
            responseBuilder
                    .skillName("null")
                    .skillDamage(0);
        }

        return responseBuilder.build();
    }
}
