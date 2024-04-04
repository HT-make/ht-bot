package com.htmake.htbot.domain.equipment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Weapon {
    @Id
    @Column(name = "item_id")
    private String id;

    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "item_level", nullable = false)
    private int level;

    @Column(name = "item_damage", nullable = false)
    private int damage;

    @Column(name = "item_health", nullable = false)
    private int health;

    @Column(name = "item_defence", nullable = false)
    private int defence;

    @Column(name = "item_critical_chance", nullable = false)
    private int criticalChance;

    @Column(name = "item_critical_damage", nullable = false)
    private int criticalDamage;

    @Column(name = "item_mana", nullable = false)
    private int mana;

    @Column(name = "item_gold", nullable = false)
    private int gold;
}
