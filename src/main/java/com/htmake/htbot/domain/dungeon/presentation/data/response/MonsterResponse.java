package com.htmake.htbot.domain.dungeon.presentation.data.response;

import com.htmake.htbot.domain.monster.entity.Monster;
import com.htmake.htbot.domain.monster.entity.MonsterSkill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private int skillChance;

    private List<MonsterSkillResponse> skillList;

    public static MonsterResponse toResponse(Monster monster) {
        List<MonsterSkill> monsterSkillList = monster.getMonsterSkillList();
        List<MonsterSkillResponse> skillList = new ArrayList<>();

        if (monsterSkillList != null) {
            skillList = monsterSkillList.stream()
                    .map(MonsterSkillResponse::toResponse)
                    .collect(Collectors.toList());
        }

        return MonsterResponse.builder()
                .id(monster.getId())
                .name(monster.getName())
                .level(monster.getLevel())
                .damage(monster.getDamage())
                .health(monster.getHealth())
                .defence(monster.getDefence())
                .skillChance(monster.getSkillChance())
                .skillList(skillList)
                .build();
    }
}
