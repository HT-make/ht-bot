package com.htmake.htbot.domain.monster.entity;

import com.htmake.htbot.domain.dungeon.entity.Dungeon;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Monster {

    @Id
    @Column(name = "monster_id")
    private String id;

    @Column(name = "monster_name", nullable = false, unique = true)
    private String name;

    @Column(name = "monster_level", nullable = false)
    private int level;

    @Column(name = "monster_damage", nullable = false)
    private int damage;

    @Column(name = "monster_health", nullable = false)
    private int health;

    @Column(name = "monster_defence", nullable = false)
    private int defence;

    @Column(name = "monster_critical_chance", nullable = false)
    private int criticalChance;

    @Column(name = "monster_critical_damage", nullable = false)
    private int criticalDamage;

    @Column(name = "monster_exp", nullable = false)
    private int exp;

    @Column(name = "monster_gold", nullable = false)
    private int gold;

    @Column(name = "monster_skill_chance", nullable = false)
    private int skillChance;

    @Column(name = "boss_gem", nullable = false)
    private int gem;

    @Column(name = "boss_coin", nullable = false)
    private int bossCoin;

    @OneToMany(mappedBy = "monster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DropItem> dropItems;

    @OneToMany(mappedBy = "monster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MonsterSkill> monsterSkillList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dungeon_id")
    private Dungeon dungeon;
}
