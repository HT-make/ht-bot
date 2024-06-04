package com.htmake.htbot.discord.commands.battle.data.status;

import com.htmake.htbot.discord.skillAction.condition.Condition;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class BasicStatus {

    private int level;

    private String name;

    private int damage;

    private int health;

    private int defence;

    private int criticalChance;

    private int criticalDamage;

    private Map<String, Condition> conditionMap;

    public BasicStatus(int level, String name, int damage, int health, int defence, int criticalChance, int criticalDamage) {
        this.level = level;
        this.name = name;
        this.damage = damage;
        this.health = health;
        this.defence = defence;
        this.criticalChance = criticalChance;
        this.criticalDamage = criticalDamage;
        this.conditionMap = new HashMap<>();
    }
}
