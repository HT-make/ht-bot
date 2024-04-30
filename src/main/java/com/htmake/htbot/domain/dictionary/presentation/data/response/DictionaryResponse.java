package com.htmake.htbot.domain.dictionary.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryResponse {

    private String name;

    private int level;

    private int damage;

    private int health;

    private int defence;

    private int criticalChance;

    private int criticalDamage;

    private int mana;

    private int gold;
}
