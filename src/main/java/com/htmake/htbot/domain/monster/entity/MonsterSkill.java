package com.htmake.htbot.domain.monster.entity;

import com.htmake.htbot.discord.skillAction.type.SkillType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonsterSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monster_skill_id")
    private Long id;

    @Column(name = "monster_skill_name", nullable = false)
    private String name;

    @Column(name = "monster_skill_damage")
    private int damage;

    @Column(name = "monster_skill_effect")
    private String effect;

    @Column(name = "monster_skill_chance", nullable = false)
    private int chance;

    @Column(name = "monster_skill_type")
    @Enumerated(EnumType.STRING)
    private SkillType skillType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monster_id")
    private Monster monster;
}
