package com.htmake.htbot.domain.dungeon.presentation.data.response;

import com.htmake.htbot.domain.dungeon.entity.Monster;
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

    private int exp;

    private int gold;

    public static MonsterResponse toResponse(Monster monster) {
        return MonsterResponse.builder()
                .id(monster.getId())
                .name(monster.getName())
                .level(monster.getLevel())
                .damage(monster.getDamage())
                .health(monster.getHealth())
                .defence(monster.getDefence())
                .exp(monster.getExp())
                .gold(monster.getGold())
                .build();
    }
}
